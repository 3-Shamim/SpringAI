package fyi.shamim.chatmemorywithspringaiv1.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Created by IntelliJ IDEA.
 * User: Md Shamim
 * Date: 6/6/26
 * Email: mdshamim723@gmail.com
 */

@Configuration
public class ChatClientProvider {

    @Bean
    public ChatClient chatClient(OpenAiChatModel model) {

        return ChatClient.builder(model).build();
    }

    @Primary
    @Bean
    public ChatMemory chatMemory(ChatMemoryRepository chatMemoryRepository) {

        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(chatMemoryRepository)
                .build();
    }

    @Bean
    public ChatClient chatClientWithChatMemory(OpenAiChatModel model, ChatMemory chatMemory) {

        return ChatClient.builder(model)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }

    @Bean
    public ChatMemory customChatMemory(ChatMemoryRepository chatMemoryRepository) {

        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(chatMemoryRepository)
                // This will ensure only 3 messages in memory for per key/conversation_id
                .maxMessages(3) // default 20
                .build();
    }

    @Bean
    public ChatClient chatClientWithCustomChatMemory(OpenAiChatModel model,
                                                     @Qualifier("customChatMemory")
                                                     ChatMemory customChatMemory) {

        return ChatClient.builder(model)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(customChatMemory).build())
                .build();
    }

    @Bean
    public ChatMemory customInMemoryChatMemory() {

        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(new InMemoryChatMemoryRepository())
                // This will ensure only 10 messages in memory for per key/conversation_id
                .maxMessages(10)
                .build();
    }

    @Bean
    public ChatClient chatClientWithCustomInMemoryChatMemory(OpenAiChatModel model,
                                                             @Qualifier("customInMemoryChatMemory")
                                                             ChatMemory chatMemory) {

        return ChatClient.builder(model)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }

    @Bean
    public ChatClient chatClientWithPromptChatMemory(OpenAiChatModel model, ChatMemory chatMemory) {

        return ChatClient.builder(model)
                // PromptChatMemoryAdvisor is deprecated
                .defaultAdvisors(PromptChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }

}
