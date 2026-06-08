package fyi.shamim.chatmemorypgvector.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.VectorStoreChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

/**
 * Created by IntelliJ IDEA.
 * User: Md Shamim
 * Date: 6/8/26
 * Email: mdshamim723@gmail.com
 */

@Configuration
public class ChatClientProvider {

    @Value("classpath:templates/vector-store-system-prompt.st")
    private Resource vectorStoreSystemPrompt;

    @Bean
    public ChatClient chatClient(OpenAiChatModel model) {

        return ChatClient.builder(model).build();
    }

    @Bean
    public ChatClient chatClientWithMemory(OpenAiChatModel model, VectorStore vectorStore, ChatMemory chatMemory) {

        return ChatClient.builder(model)
                .defaultAdvisors(
                        VectorStoreChatMemoryAdvisor.builder(vectorStore)
                                // Add custom system prompt template
                                .systemPromptTemplate(new PromptTemplate(vectorStoreSystemPrompt))
                                .defaultTopK(10)
                                .build(),
                        MessageChatMemoryAdvisor.builder(chatMemory).build()
                )
                .build();
    }

}
