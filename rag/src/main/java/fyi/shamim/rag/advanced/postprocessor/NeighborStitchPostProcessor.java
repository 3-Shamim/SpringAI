package fyi.shamim.rag.advanced.postprocessor;

import fyi.shamim.rag.advanced.constant.RagConstant;
import fyi.shamim.rag.advanced.postprocessor.record.Target;
import fyi.shamim.rag.config.RagConfigData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.postretrieval.document.DocumentPostProcessor;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by IntelliJ IDEA.
 * User: Md Shamim
 * Date: 6/17/26
 * Email: mdshamim723@gmail.com
 */

/*
 * Adds neighbor chunks (chunk_index +- N) for each retrieve docs, by source.
 * Assumes metadata contains: souce (String), chunk_index (Number/int).
 * */

@Slf4j
@Component
public class NeighborStitchPostProcessor implements DocumentPostProcessor {

    private final VectorStore vectorStore;
    private final RagConfigData ragConfigData;

    public NeighborStitchPostProcessor(@Qualifier("ragAdvancedVectorStore")
                                        VectorStore vectorStore,
                                       RagConfigData ragConfigData) {

        this.vectorStore = vectorStore;
        this.ragConfigData = ragConfigData;
    }

    @Override
    public List<Document> process(Query query, List<Document> documents) {

        if (CollectionUtils.isEmpty(documents)) {
            return documents;
        }

        Set<Target> targets = getTargets(documents);
        List<Document> neighbors = getNeighbors(targets);

        List<Document> mergedDocuments = mergeDocuments(documents, neighbors);

        return deDupBySourceIndexPreserveOrder(mergedDocuments);
    }

    private Set<Target> getTargets(List<Document> documents) {

        return documents.stream().flatMap(d -> {

                    Set<Target> targets = new HashSet<>();

                    String source = String.valueOf(d.getMetadata().get(RagConstant.SOURCE));
                    Object indexObj = d.getMetadata().get(RagConstant.CHUNK_INDEX);

                    if (source == null || indexObj == null) {
                        return null;
                    }

                    int index = (indexObj instanceof Number)
                            ? ((Number) indexObj).intValue()
                            : Integer.parseInt(String.valueOf(indexObj));

                    for (int i = 1; i <= ragConfigData.getRadius(); i++) {
                        targets.add(new Target(source, index - i));
                        targets.add(new Target(source, index + i));
                    }

                    return targets.stream();
                }).filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    private List<Document> getNeighbors(Set<Target> targets) {

        List<Document> neighbors = new ArrayList<>();

        for (Target target : targets) {

            var searchRequest = getSearchRequest(target);
            var hits = vectorStore.similaritySearch(searchRequest);

            if (hits.isEmpty()) {
                continue;
            }

            neighbors.addAll(hits);

        }

        return neighbors;
    }

    private SearchRequest getSearchRequest(Target target) {

        String filter = "%s == '%s' && %s == %d".formatted(
                RagConstant.SOURCE, target.source().replace("'", "\\'"),
                RagConstant.CHUNK_INDEX, target.index()
        );

        return SearchRequest.builder()
                .query("__neighbors__")
                .topK(1)
                .similarityThreshold(0.0)
                .filterExpression(filter)
                .build();
    }

    private List<Document> mergeDocuments(List<Document> documents, List<Document> neighbors) {

        if (!CollectionUtils.isEmpty(neighbors)) {
            documents.addAll(neighbors);
        }

        return documents;
    }

    private List<Document> deDupBySourceIndexPreserveOrder(List<Document> mergedDocuments) {

        Set<String> seen = new HashSet<>();
        List<Document> documents = new ArrayList<>(mergedDocuments.size());

        for (Document document : mergedDocuments) {

            String source = String.valueOf(document.getMetadata().get(RagConstant.SOURCE));
            Object indexObj = document.getMetadata().get(RagConstant.CHUNK_INDEX);

            String key = source + "#" + indexObj;

            if (seen.add(key)) {
                documents.add(document);
            }

        }

        return documents;
    }

}
