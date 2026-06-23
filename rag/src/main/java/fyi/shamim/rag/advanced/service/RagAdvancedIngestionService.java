package fyi.shamim.rag.advanced.service;

import fyi.shamim.rag.advanced.constant.RagConstant;
import fyi.shamim.rag.advanced.exception.RagException;
import fyi.shamim.rag.config.PgVectorStoreConfigData;
import fyi.shamim.rag.config.RagConfigData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.ParagraphPdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Md Shamim
 * Date: 6/14/26
 * Email: mdshamim723@gmail.com
 */

@Slf4j
@Service
public class RagAdvancedIngestionService {

    private final VectorStore vectorStore;
    private final JdbcTemplate jdbcTemplate;
    private final RagConfigData ragConfigData;
    private final PdfDocumentReaderConfig pdfDocumentReaderConfig;
    private final TokenTextSplitter tokenTextSplitter;
    private final PgVectorStoreConfigData pgVectorStoreConfigData;

    public RagAdvancedIngestionService(@Qualifier("ragAdvancedVectorStore")
                                       VectorStore vectorStore,
                                       JdbcTemplate jdbcTemplate,
                                       RagConfigData ragConfigData,
                                       TokenTextSplitter tokenTextSplitter,
                                       PgVectorStoreConfigData pgVectorStoreConfigData) {

        this.vectorStore = vectorStore;
        this.jdbcTemplate = jdbcTemplate;
        this.ragConfigData = ragConfigData;

        RagConfigData.PdfProperties pdf = ragConfigData.getPdf();
        this.pdfDocumentReaderConfig = PdfDocumentReaderConfig.builder()
                .withPagesPerDocument(pdf.getPagesPerDocument())
                .withPageExtractedTextFormatter(
                        ExtractedTextFormatter.builder()
                                .withLeftAlignment(pdf.isLeftAlignment())
                                .withNumberOfTopTextLinesToDelete(pdf.getNumberOfTopTextLineToDelete())
                                .withNumberOfBottomTextLinesToDelete(pdf.getNumberOfBottomTextLineToDelete())
                                .build()
                )
                .build();
        this.tokenTextSplitter = tokenTextSplitter;
        this.pgVectorStoreConfigData = pgVectorStoreConfigData;
    }

    public void initializePgVectorStore() {

        if (skipIngest(jdbcTemplate)) {
            return;
        }

        var pdfResources = getPdfResources();

        if (pdfResources == null) {
            return;
        }

        ingestDocumentChunksToVectorStore(pdfResources);

    }

    public void upsertOneByPath(Path path) {

        log.info("Processing new path for ingestion: {}", path);

        FileSystemResource resource = new FileSystemResource(path.toFile());
        String source = resource.getFilename();
        String checksum = sha265Hex(resource);

        Long count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM %s WHERE metadata->>'source' = ? AND metadata->>'checksum' = ?".formatted(
                        pgVectorStoreConfigData.getTableNameForRag()
                ),
                Long.class,
                source,
                checksum
        );

        if (count != null && count > 0) {
            log.warn("Skipping new entry the checksum is already present in DB.");
            return;
        }

        deleteBySource(source);
        ingestDocumentChunksToVectorStore(new Resource[]{resource});

    }

    public void deleteBySource(String source) {

        jdbcTemplate.update(
                "DELETE FROM %s WHERE metadata->>'source' = ?".formatted(pgVectorStoreConfigData.getTableNameForRag()),
                source
        );

        log.info("Deleted source: {} from vector store", source);

    }

    private void ingestDocumentChunksToVectorStore(Resource[] pdfResources) {

        var documents = getDocuments(pdfResources);
        var chunks = getChunks(documents);

        addChunkIndex(chunks);

        vectorStore.add(chunks);

        log.info("Ingested {} chunks to PgVector", chunks.size());

    }

    private void addChunkIndex(List<Document> chunks) {

        Map<String, Integer> counters = new HashMap<>();

        for (Document chunk : chunks) {

            String source = String.valueOf(chunk.getMetadata().getOrDefault(RagConstant.SOURCE, RagConstant.UNKNOWN));
            int index = counters.merge(source, 1, Integer::sum) - 1;
            chunk.getMetadata().put(RagConstant.CHUNK_INDEX, index);

        }

    }

    private List<Document> getChunks(List<Document> documents) {
        return tokenTextSplitter.apply(documents);
    }

    private List<Document> getDocuments(Resource[] pdfResources) {

        List<Document> documents = new ArrayList<>();

        for (Resource resource : pdfResources) {
            List<Document> parts = getDocumentParts(resource);
            addMetadata(resource, parts);
            documents.addAll(parts);
        }

        return documents;
    }

    private void addMetadata(Resource resource, List<Document> parts) {

        for (Document part : parts) {
            part.getMetadata().putIfAbsent(RagConstant.SOURCE, resource.getFilename());
            part.getMetadata().putIfAbsent(
                    RagConstant.DOC_TYPE,
                    Objects.requireNonNull(resource.getFilename()).substring(0, resource.getFilename().lastIndexOf("."))
            );
            part.getMetadata().putIfAbsent(RagConstant.UPDATED_AT, ZonedDateTime.now().toLocalDate().toString());
            String checksum = sha265Hex(resource);
            part.getMetadata().putIfAbsent(RagConstant.CHECKSUM, checksum);
        }

    }

    private List<Document> getDocumentParts(Resource resource) {

        List<Document> parts;

        if (RagConstant.PARAGRAPH.equalsIgnoreCase(ragConfigData.getPdf().getMode())) {

            // Paragraph mode relies on PDF Outlie/TOC; not all PDFs have it.
            parts = new ParagraphPdfDocumentReader(resource, pdfDocumentReaderConfig).read();

        } else { // page

            parts = new PagePdfDocumentReader(resource, pdfDocumentReaderConfig).read();

        }

        return parts;
    }

    private Resource[] getPdfResources() {

        try {

            var resolver = new PathMatchingResourcePatternResolver();
            String path = ragConfigData.getPdf().getPath();
            Resource[] pdfResources = resolver.getResources(path);

            if (pdfResources.length == 0) {
                log.warn("No PDFs found!");
                return null;
            }

            return pdfResources;
        } catch (IOException e) {
            throw new RagException("Could not resolve PDF resources!", e);
        }

    }

    private boolean skipIngest(JdbcTemplate jdbcTemplate) {

        if (ragConfigData.isForceRebuild()) {
            log.info("force-rebuild = true -> truncating {}", pgVectorStoreConfigData.getTableNameForRag());
            jdbcTemplate.update("TRUNCATE TABLE " + pgVectorStoreConfigData.getTableNameForRag());
        } else {

            Long count = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM " + pgVectorStoreConfigData.getTableNameForRag(),
                    Long.class
            );

            if (count != null && count > 0) {
                log.info(
                        "Vector data is already populated ({} rows) to {}. Skipping ingestion.",
                        count,
                        pgVectorStoreConfigData.getTableNameForRag()
                );
                return true;
            }

        }

        return false;
    }

    private String sha265Hex(Resource resource) {

        try {
            final MessageDigest messageDigest = newMessageDigest("SHA-265");

            try (InputStream inputStream = resource.getInputStream();
                 DigestInputStream digestInputStream = new DigestInputStream(inputStream, messageDigest)) {

                digestInputStream.transferTo(OutputStream.nullOutputStream());
            }

            return HexFormat.of().formatHex(messageDigest.digest());
        } catch (IOException e) {
            throw new RagException("Error creating sha265Hex for resource " + resource.getFilename(), e);
        }

    }

    private MessageDigest newMessageDigest(String algorithm) {
        try {
            return MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new RagException("Missing JCA provider for : " + algorithm, e);
        }
    }

}
