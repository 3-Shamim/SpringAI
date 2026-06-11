package fyi.shamim.rag.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Created by IntelliJ IDEA.
 * User: Md Shamim
 * Date: 6/11/26
 * Email: mdshamim723@gmail.com
 */

@Configuration
public class ChatClientProvider {

    @Bean
    public ChatClient openAIChatClient(OpenAiChatModel model) {
        return ChatClient.builder(model).build();
    }

    @Bean
    @Primary
    public ChatClient ollamaChatClient(OllamaChatModel model) {
        return ChatClient.builder(model).build();
    }

}
