package fyi.shamim.advisor.advisor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisor;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisorChain;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Md Shamim
 * Date: 5/31/26
 * Email: mdshamim723@gmail.com
 */

@Slf4j
@Component
public class SystemPromptAdvisor implements CallAdvisor, StreamAdvisor {

    @Override
    public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain callAdvisorChain) {

        chatClientRequest = updateSystemMessage(chatClientRequest);

        return callAdvisorChain.nextCall(chatClientRequest);
    }

    @Override
    public Flux<ChatClientResponse> adviseStream(ChatClientRequest chatClientRequest, StreamAdvisorChain streamAdvisorChain) {

        chatClientRequest = updateSystemMessage(chatClientRequest);

        return streamAdvisorChain.nextStream(chatClientRequest);
    }

    @Override
    public String getName() {
        return "SystemPromptAdvisor";
    }

    @Override
    public int getOrder() {
        return 0;
    }

    private ChatClientRequest updateSystemMessage(ChatClientRequest chatClientRequest) {

        ArrayList<Message> existingMessages = new ArrayList<>(chatClientRequest.prompt().getUserMessages());
        SystemMessage systemMessage = chatClientRequest.prompt().getSystemMessage();

        existingMessages.add(
                new SystemMessage(
                        """
                                You are very specialize movie explainer, don't explain anything else.
                                Always explain within 200 words. %s
                                """.formatted(systemMessage.getText())
                )
        );

        chatClientRequest = chatClientRequest.mutate()
                .prompt(
                        Prompt
                                .builder()
                                .messages(existingMessages)
                                .build()
                )
                .build();

        log.info(
                "System message is updated as [{}]",
                chatClientRequest.prompt().getSystemMessage().getText()
        );

        return chatClientRequest;
    }

}
