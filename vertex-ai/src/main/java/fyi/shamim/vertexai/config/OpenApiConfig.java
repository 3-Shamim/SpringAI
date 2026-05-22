package fyi.shamim.vertexai.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Vertex AI API")
                        .description("Spring AI — Vertex AI Gemini chat endpoints")
                        .version("1.0.0"));
    }
}
