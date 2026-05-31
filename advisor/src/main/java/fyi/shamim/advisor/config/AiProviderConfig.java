package fyi.shamim.advisor.config;

import fyi.shamim.advisor.advisor.ErrorWrapperAdvisor;
import fyi.shamim.advisor.advisor.SystemPromptAdvisor;
import fyi.shamim.advisor.advisor.ValidationAdvisor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SafeGuardAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Md Shamim
 * Date: 5/31/26
 * Email: mdshamim723@gmail.com
 */

@Slf4j
@Configuration
public class AiProviderConfig {

    @Bean
    public ChatClient chatClient(OpenAiChatModel model,
                                 SimpleLoggerAdvisor simpleLoggerAdvisor,
                                 SafeGuardAdvisor safeGuardAdvisor,
                                 ErrorWrapperAdvisor errorWrapperAdvisor,
                                 SystemPromptAdvisor systemPromptAdvisor,
                                 ValidationAdvisor validationAdvisor) {

        return ChatClient
                .builder(model)
                .defaultAdvisors(
                        errorWrapperAdvisor, safeGuardAdvisor, simpleLoggerAdvisor, systemPromptAdvisor,
                        validationAdvisor
                )
                .build();
    }

    @Bean
    public SimpleLoggerAdvisor simpleLoggerAdvisor() {
        return new SimpleLoggerAdvisor();
    }

    @Bean
    public SafeGuardAdvisor safeGuardAdvisor() {
        return new SafeGuardAdvisor(
                List.of("password", "api key")
        );
    }

}
