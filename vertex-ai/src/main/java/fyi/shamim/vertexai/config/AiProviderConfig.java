package fyi.shamim.vertexai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.vertexai.gemini.VertexAiGeminiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by IntelliJ IDEA.
 * User: Md Shamim
 * Date: 5/21/26
 * Email: mdshamim723@gmail.com
 */

@Configuration
public class AiProviderConfig {

    @Bean
    public ChatClient vertexAiChatClient(VertexAiGeminiChatModel model) {
        return ChatClient.builder(model).build();
    }

}
