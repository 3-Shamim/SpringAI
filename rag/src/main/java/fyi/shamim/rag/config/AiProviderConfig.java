package fyi.shamim.rag.config;

import fyi.shamim.rag.advanced.postprocessor.CitationHeaderPostProcessor;
import fyi.shamim.rag.advanced.postprocessor.NeighborStitchPostProcessor;
import fyi.shamim.rag.advanced.preprocessor.DomainSynonymTransformer;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Created by IntelliJ IDEA.
 * User: Md Shamim
 * Date: 6/11/26
 * Email: mdshamim723@gmail.com
 */

@Configuration
public class AiProviderConfig {

    @Bean
    public ChatClient openAIChatClient(OpenAiChatModel model) {
        return ChatClient.builder(model).build();
    }

    @Bean
    @Primary
    public ChatClient ollamaChatClient(OllamaChatModel model) {
        return ChatClient.builder(model).build();
    }

    @Bean
    public ChatClient openAiSimpleRagChatClient(OpenAiChatModel model,
                                                SimpleVectorStore simpleVectorStore,
                                                RagConfigData data,
                                                SimpleLoggerAdvisor simpleLoggerAdvisor) {

        return ChatClient.builder(model)
                .defaultAdvisors(
                        QuestionAnswerAdvisor.builder(simpleVectorStore)
                                .searchRequest(
                                        SearchRequest.builder()
                                                .topK(data.getTopK())
                                                .similarityThreshold(data.getSimilarityThreshold())
                                                .build()
                                )
                                .build(),
                        simpleLoggerAdvisor
                )
                .build();
    }

    @Bean
    public VectorStore chatMemoryPgVector(JdbcTemplate jdbcTemplate,
                                          PgVectorStoreConfigData configData,
                                          @Qualifier("openAiEmbeddingModel")
                                          EmbeddingModel embeddingModel) {

        return PgVectorStore.builder(jdbcTemplate, embeddingModel)
                .vectorTableName(configData.getTableNameForChatMemory())
                .initializeSchema(configData.isInitializeSchema())
                .indexType(configData.getIndexType())
                .distanceType(configData.getDistanceType())
                .dimensions(configData.getDimensions())
                .maxDocumentBatchSize(configData.getMaxDocumentBatchSize())
                .build();
    }

    @Bean
    public VectorStore ragAdvancedVectorStore(JdbcTemplate jdbcTemplate,
                                              PgVectorStoreConfigData configData,
                                              @Qualifier("openAiEmbeddingModel")
                                              EmbeddingModel embeddingModel) {

        return PgVectorStore.builder(jdbcTemplate, embeddingModel)
                .vectorTableName(configData.getTableNameForRag())
                .initializeSchema(configData.isInitializeSchema())
                .indexType(configData.getIndexType())
                .distanceType(configData.getDistanceType())
                .dimensions(configData.getDimensions())
                .maxDocumentBatchSize(configData.getMaxDocumentBatchSize())
                .build();
    }

    @Bean
    public RetrievalAugmentationAdvisor retrievalAugmentationAdvisor(@Qualifier("ragAdvancedVectorStore")
                                                                     VectorStore vectorStore,
                                                                     RagConfigData configData,
                                                                     DomainSynonymTransformer domainSynonymTransformer,
                                                                     NeighborStitchPostProcessor neighborStitchPostProcessor,
                                                                     CitationHeaderPostProcessor citationHeaderPostProcessor) {

        return RetrievalAugmentationAdvisor.builder()
                .queryTransformers(domainSynonymTransformer)
                .documentRetriever(
                        VectorStoreDocumentRetriever.builder()
                                .vectorStore(vectorStore)
                                .topK(configData.getTopK())
                                .similarityThreshold(configData.getSimilarityThreshold())
                                .build()
                )
                .documentPostProcessors(neighborStitchPostProcessor, citationHeaderPostProcessor)
                .queryAugmenter(
                        ContextualQueryAugmenter.builder()
                                .allowEmptyContext(false)
                                .build()
                )
                .build();
    }

    @Bean
    public ChatClient openAiAdvancedRAGChatClient(OpenAiChatModel model,
                                                  @Qualifier("retrievalAugmentationAdvisor")
                                                  RetrievalAugmentationAdvisor retrievalAugmentationAdvisor) {

        return ChatClient.builder(model)
                .defaultAdvisors(retrievalAugmentationAdvisor)
                .build();
    }

    @Primary
    @Bean
    public EmbeddingModel primaryOpenAiEmbeddingModel(@Qualifier("openAiEmbeddingModel")
                                                      EmbeddingModel embeddingModel) {

        return embeddingModel;
    }

    @Bean
    public TokenTextSplitter tokenTextSplitter(RagConfigData ragConfigData) {

        return TokenTextSplitter.builder()
                .withChunkSize(ragConfigData.getChunk().getSize())
                .withMinChunkSizeChars(ragConfigData.getChunk().getMinChunkSize())
                .withMinChunkLengthToEmbed(ragConfigData.getChunk().getMinChunkToEmbed())
                .withMaxNumChunks(ragConfigData.getChunk().getMaxChunkSize())
                .withKeepSeparator(ragConfigData.getChunk().isKeepSeparator())
                .build();
    }

    @Bean
    public SimpleLoggerAdvisor simpleLoggerAdvisor() {
        return new SimpleLoggerAdvisor();
    }

}
