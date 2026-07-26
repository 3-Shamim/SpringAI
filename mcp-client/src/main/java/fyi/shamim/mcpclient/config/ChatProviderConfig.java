package fyi.shamim.mcpclient.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.stream.Stream;

/**
 * Created by IntelliJ IDEA.
 * User: Md Shamim
 * Date: 7/26/26
 * Email: mdshamim723@gmail.com
 */

@Slf4j
@Configuration
public class ChatProviderConfig {

    @Bean
    public ChatClient chatClient(OpenAiChatModel model, ToolCallbackProvider toolCallbackProvider) {

        Stream.of(toolCallbackProvider.getToolCallbacks()).forEach(tool -> {
            log.info("Tool callback found: {}", tool.getToolDefinition());
        });

        return ChatClient.builder(model)
                .defaultTools(toolCallbackProvider)
                .build();
    }

}
