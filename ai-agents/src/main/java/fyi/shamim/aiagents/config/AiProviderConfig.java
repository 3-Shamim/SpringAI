package fyi.shamim.aiagents.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Created by IntelliJ IDEA.
 * User: Md Shamim
 * Date: 6/24/26
 * Email: mdshamim723@gmail.com
 */

@Slf4j
@Configuration
public class AiProviderConfig {

    @Value("${spring.ai.openai.chat.options.model-version}")
    private String diagramChatModelVersion;

    @Bean
    public ChatClient chatClient(OpenAiChatModel model) {

        return ChatClient.builder(model).build();
    }

    @Bean
    public ChatClient chatClientForDiagram(OpenAiChatModel model) {

        return ChatClient.builder(model)
                .defaultOptions(ChatOptions.builder().model(diagramChatModelVersion).build())
                .build();
    }

    @Bean
    public VectorStore customVectorStore(PgVectorStoreConfigData configData,
                                         JdbcTemplate jdbcTemplate,
                                         @Qualifier("openAiEmbeddingModel")
                                         EmbeddingModel embeddingModel) {

        return PgVectorStore.builder(jdbcTemplate, embeddingModel)
                .vectorTableName(configData.getTableName())
                .initializeSchema(configData.isInitializeSchema())
                .indexType(configData.getIndexType())
                .distanceType(configData.getDistanceType())
                .dimensions(configData.getDimensions())
                .maxDocumentBatchSize(configData.getMaxDocumentBatchSize())
                .build();
    }

}
