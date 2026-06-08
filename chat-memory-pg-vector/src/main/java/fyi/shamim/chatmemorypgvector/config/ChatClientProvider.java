package fyi.shamim.chatmemorypgvector.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.VectorStoreChatMemoryAdvisor;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by IntelliJ IDEA.
 * User: Md Shamim
 * Date: 6/8/26
 * Email: mdshamim723@gmail.com
 */

@Configuration
public class ChatClientProvider {

    @Bean
    public ChatClient chatClient(OpenAiChatModel model) {

        return ChatClient.builder(model).build();
    }

    @Bean
    public ChatClient chatClientWithMemory(OpenAiChatModel model, VectorStore vectorStore) {

        return ChatClient.builder(model)
                .defaultAdvisors(VectorStoreChatMemoryAdvisor.builder(vectorStore).build())
                .build();
    }

}
