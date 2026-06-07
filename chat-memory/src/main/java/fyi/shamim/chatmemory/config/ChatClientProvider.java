package fyi.shamim.chatmemory.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

    @Bean
    public ChatClient chatClientWithChatMemory(OpenAiChatModel model, ChatMemory chatMemory) {

        return ChatClient.builder(model)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }

    @Bean
    public ChatMemoryRepository inMemoryChatMemoryRepository() {
        return new InMemoryChatMemoryRepository();
    }

    @Bean
    ChatMemory customInMemoryChatMemory(ChatMemoryRepository inMemoryChatMemoryRepository) {

        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(inMemoryChatMemoryRepository)
                .maxMessages(10)
                .build();
    }

    @Bean
    public ChatClient chatClientWithCustomInMemoryChatMemory(OpenAiChatModel model, ChatMemory customInMemoryChatMemory) {

        return ChatClient.builder(model)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(customInMemoryChatMemory).build())
                .build();
    }

}
