package fyi.shamim.aiagents.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by IntelliJ IDEA.
 * User: Md Shamim
 * Date: 6/24/26
 * Email: mdshamim723@gmail.com
 */

@Getter
@Setter
@Configuration
@ConfigurationProperties("app.agent")
public class AiAgentConfigData {

    private String uploadDir;
    private DiagramToolProperties diagramTool;
    private PostureToolProperties postureTool;
    private WebToolProperties webTool;
    private RagToolProperties ragTool;

    @Getter
    @Setter
    public static class DiagramToolProperties {
        private double temperature;
    }

    @Getter
    @Setter
    public static class PostureToolProperties {
        private String url;
        private String env;
    }

    @Getter
    @Setter
    public static class WebToolProperties {

        private int topK;
        private GoogleVertexSearchProperties googleVertexSearch;
        private OwaspProperties owasp;

    }

    @Getter
    @Setter
    public static class GoogleVertexSearchProperties {

        private String endpointBaseUrl;
        private String servingConfig;

    }

    @Getter
    @Setter
    public static class OwaspProperties {

        private String cheatSheetProtocol;
        private String cheatSheetUrl;
        private String asvsUrl;

    }

    @Getter
    @Setter
    public static class RagToolProperties {

        private double similarityThreshold;
        private int minTopK;
        private int maxTopK;
        private int defaultTopK;

    }

}
