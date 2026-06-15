package fyi.shamim.rag.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by IntelliJ IDEA.
 * User: Md Shamim
 * Date: 6/14/26
 * Email: mdshamim723@gmail.com
 */

@Getter
@Setter
@Configuration
@ConfigurationProperties("spring.ai.vectorstore.pgvector")
public class PgVectorStoreConfigData {

    private String tableNameForChatMemory;
    private String tableNameForRag;
    private boolean initializeSchema;
    private PgVectorStore.PgIndexType indexType;
    private PgVectorStore.PgDistanceType distanceType;
    private int dimensions;
    private int maxDocumentBatchSize;

}
