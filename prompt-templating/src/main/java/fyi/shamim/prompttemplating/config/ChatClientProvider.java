package fyi.shamim.prompttemplating.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by IntelliJ IDEA.
 * User: Md Shamim
 * Date: 6/2/26
 * Email: mdshamim723@gmail.com
 */

@Slf4j
@Configuration
public class ChatClientProvider {

    @Bean
    public ChatClient chatClient(OpenAiChatModel model) {

        return ChatClient.builder(model).build();
    }

}
