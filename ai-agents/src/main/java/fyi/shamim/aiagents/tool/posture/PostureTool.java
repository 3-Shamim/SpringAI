package fyi.shamim.aiagents.tool.posture;

import fyi.shamim.aiagents.config.AiAgentConfigData;
import fyi.shamim.aiagents.tool.posture.record.PostureArgs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Md Shamim
 * Date: 6/28/26
 * Email: mdshamim723@gmail.com
 */

@Slf4j
@Component
public class PostureTool {

    private final WebClient webClient;
    private final AiAgentConfigData.PostureToolProperties postureToolProperties;

    public PostureTool(WebClient.Builder webClientBuilder,
                       AiAgentConfigData aiAgentConfigData) {

        this.webClient = webClientBuilder.baseUrl(aiAgentConfigData.getPostureTool().getUrl()).build();
        this.postureToolProperties = aiAgentConfigData.getPostureTool();
    }

    @Tool(
            name = "security_posture",
            description = "Get security posture for a service (internetFacing, data classes, TLS, vulnerabilities, secrets)."
    )
    public Map<String, Object> getSecurityPosture(PostureArgs args) {

        try {

            if (args == null) {
                return Collections.emptyMap();
            }

            return this.webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/posture/{serviceId}")
                            .queryParam("env", postureToolProperties.getEnv())
                            .build(args.serviceId()))
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                    })
                    .block();
        } catch (Exception e) {
            return Map.of("error", "POSTURE_SERVICE_CALL_FIALED", "message", e.getMessage());
        }

    }

}
