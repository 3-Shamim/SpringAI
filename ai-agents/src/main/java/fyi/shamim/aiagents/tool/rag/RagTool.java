package fyi.shamim.aiagents.tool.rag;

import fyi.shamim.aiagents.config.AiAgentConfigData;
import fyi.shamim.aiagents.tool.rag.record.RagArgs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Md Shamim
 * Date: 6/29/26
 * Email: mdshamim723@gmail.com
 */

@Slf4j
@Component
public class RagTool {

    private final VectorStore vectorStore;
    private final AiAgentConfigData.RagToolProperties ragToolProperties;

    public RagTool(@Qualifier("customVectorStore")
                   VectorStore vectorStore,
                   AiAgentConfigData aiAgentConfigData) {

        this.vectorStore = vectorStore;
        this.ragToolProperties = aiAgentConfigData.getRagTool();
    }

    @Tool(
            name = "rag_query",
            description = "Query internal security policies and checklists with RAG; return concise quotes + citations."
    )
    Map<String, Object> query(RagArgs ragArgs) {

        try {

            if (ragArgs == null) {
                return Collections.emptyMap();
            }

            log.info("Calling rag_query tool with question: {} and topK: {}", ragArgs.question(), ragArgs.topK());

            SearchRequest.Builder searchRequestBuilder = getSearchRequestBuilder(ragArgs, getTopK(ragArgs));
            var hits = vectorStore.similaritySearch(searchRequestBuilder.build());

            return Map.of("matches", getMatches(hits));
        } catch (Exception e) {
            return Map.of("error", "RAG_SEARCH_FAILED", "message", e.getMessage());
        }

    }

    private int getTopK(RagArgs ragArgs) {

        return Math.max(
                this.ragToolProperties.getMinTopK(),
                Math.min(
                        this.ragToolProperties.getMaxTopK(),
                        Optional.ofNullable(ragArgs.topK()).orElse(this.ragToolProperties.getDefaultTopK())
                )
        );
    }

    private SearchRequest.Builder getSearchRequestBuilder(RagArgs ragArgs, int topK) {

        return SearchRequest.builder()
                .query(Objects.requireNonNull(ragArgs.question(), "question is required"))
                .topK(topK)
                .similarityThreshold(this.ragToolProperties.getSimilarityThreshold());
    }

    private List<Map<String, Object>> getMatches(List<Document> hits) {

        return hits.stream().map(document -> Map.of(
                "docId", document.getId(),
                "title", document.getMetadata().getOrDefault("title", ""),
                "score", document.getScore(),
                "quote", document.getFormattedContent(),
                "source", Map.of(
                        "uri", document.getMetadata().getOrDefault("uri", ""),
                        "section", document.getMetadata().getOrDefault("section", "")
                )
        )).toList();
    }

}
