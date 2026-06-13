package fyi.shamim.rag.simple_employee_handbook.config;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by IntelliJ IDEA.
 * User: Md Shamim
 * Date: 6/11/26
 * Email: mdshamim723@gmail.com
 */

@Slf4j
@Configuration
@ConfigurationProperties(prefix = "app.rag")
@Getter
@Setter
public class SimpleRagConfigData {

    private boolean forceRebuild;
    private int topK;
    private double similarityThreshold;
    private RagChunkProperties chunk;

    @Setter
    @Getter
    static class RagChunkProperties {

        private int size;
        private int minChunkSize;
        private int minChunkToEmbed;
        private int maxChunkSize;
        private boolean keepSeparator;

    }

}
