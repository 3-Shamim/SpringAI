package fyi.shamim.streamchat.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by IntelliJ IDEA.
 * User: Md Shamim
 * Date: 5/29/26
 * Email: mdshamim723@gmail.com
 */

@Configuration
public class ChatProviderConfig {

    @Bean
    public ChatClient chatClient(OpenAiChatModel model) {

        return ChatClient.builder(model).build();
    }

}
