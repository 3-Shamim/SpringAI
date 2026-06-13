package fyi.shamim.rag.simple_employee_handbook.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Md Shamim
 * Date: 6/11/26
 * Email: mdshamim723@gmail.com
 */

@Slf4j
@Configuration
public class SimpleRagConfiguration {

    private static final String FILES_PATH = "classpath:rag/employee-handbooks/files/*.md";
    private static final String VECTOR_STORE_PATH = "data/employee-handbooks/vector-store/vectorstore.json";

    @Bean
    public TokenTextSplitter tokenTextSplitter(SimpleRagConfigData simpleRagConfigData) {
        return TokenTextSplitter.builder()
                .withChunkSize(simpleRagConfigData.getChunk().getSize())
                .withMinChunkSizeChars(simpleRagConfigData.getChunk().getMinChunkSize())
                .withMinChunkLengthToEmbed(simpleRagConfigData.getChunk().getMinChunkToEmbed())
                .withMaxNumChunks(simpleRagConfigData.getChunk().getMaxChunkSize())
                .withKeepSeparator(simpleRagConfigData.getChunk().isKeepSeparator())
                .build();
    }

    @Bean
    public SimpleVectorStore simpleVectorStore(@Qualifier("openAiEmbeddingModel")
                                               EmbeddingModel openAiEmbeddingModel,
                                               TokenTextSplitter tokenTextSplitter,
                                               SimpleRagConfigData simpleRagConfigData) throws IOException {

        var simpleVectorStore = SimpleVectorStore.builder(openAiEmbeddingModel).build();

        var vectorStoreFile = new File(VECTOR_STORE_PATH);

        if (vectorStoreFile.exists() && !simpleRagConfigData.isForceRebuild()) {

            log.info("Vector store file exists, and loading it from: {}", VECTOR_STORE_PATH);
            simpleVectorStore.load(vectorStoreFile);

            return simpleVectorStore;
        }

        log.info("Creating a new vector store file at: {}", VECTOR_STORE_PATH);

        var documents = loadDocuments();
        var chunks = getChunks(documents, tokenTextSplitter);

        simpleVectorStore.add(chunks);

        var vectorStoreDir = vectorStoreFile.getParentFile();
        if (vectorStoreDir != null && !vectorStoreDir.exists() && !vectorStoreDir.mkdirs()) {
            throw new IOException("Failed to create vector store directory: " + vectorStoreDir);
        }

        simpleVectorStore.save(vectorStoreFile);

        return simpleVectorStore;
    }

    private List<Document> getChunks(List<Document> documents, TokenTextSplitter tokenTextSplitter) {
        return tokenTextSplitter.apply(documents);
    }

    private List<Document> loadDocuments() throws IOException {

        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources(FILES_PATH);

        List<Document> documents = new ArrayList<>();

        for (Resource resource : resources) {

            TextReader reader = new TextReader(resource);
            reader.getCustomMetadata().put("category", resource.getFilename().replace(".md", ""));
            reader.getCustomMetadata().put("access_level", "public");
            reader.getCustomMetadata().put("version", "2026.01");

            documents.addAll(reader.read());

        }

        return documents;
    }

}
