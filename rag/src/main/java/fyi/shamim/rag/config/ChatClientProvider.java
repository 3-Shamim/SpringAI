package fyi.shamim.rag.config;

import fyi.shamim.rag.simple_employee_handbook.config.SimpleRagConfigData;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
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

    @Bean
    public ChatClient openAiSimpleRagChatClient(OpenAiChatModel model,
                                                SimpleVectorStore simpleVectorStore,
                                                SimpleRagConfigData data,
                                                SimpleLoggerAdvisor simpleLoggerAdvisor) {

        return ChatClient.builder(model)
                .defaultAdvisors(
                        QuestionAnswerAdvisor.builder(simpleVectorStore)
                                .searchRequest(
                                        SearchRequest.builder()
                                                .topK(data.getTopK())
                                                .similarityThreshold(data.getSimilarityThreshold())
                                                .build()
                                )
                                .build(),
                        simpleLoggerAdvisor
                )
                .build();
    }

    @Bean
    public SimpleLoggerAdvisor simpleLoggerAdvisor() {
        return new SimpleLoggerAdvisor();
    }

}
