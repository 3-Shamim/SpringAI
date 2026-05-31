package fyi.shamim.advisor.advisor;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisor;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisorChain;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

/**
 * Created by IntelliJ IDEA.
 * User: Md Shamim
 * Date: 5/31/26
 * Email: mdshamim723@gmail.com
 */

@Slf4j
@Component
public class ValidationAdvisor implements CallAdvisor, StreamAdvisor {

    private static final long MAX_TOKEN = 5000;

    @Override
    public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain callAdvisorChain) {

        validateInput(chatClientRequest);
        ChatClientResponse chatClientResponse = callAdvisorChain.nextCall(chatClientRequest);
        validateOutput(chatClientResponse);

        return chatClientResponse;
    }

    @Override
    public Flux<ChatClientResponse> adviseStream(ChatClientRequest chatClientRequest, StreamAdvisorChain streamAdvisorChain) {

        return streamAdvisorChain.nextStream(chatClientRequest);
    }

    @Override
    public String getName() {
        return "ValidationAdvisor";
    }

    @Override
    public int getOrder() {
        return 0;
    }

    private void validateInput(ChatClientRequest chatClientRequest) {

        String contents = chatClientRequest.prompt().getContents();

        if (contents.length() > MAX_TOKEN) {
            throw new IllegalArgumentException(
                    "Prompt too long: exceeds %d characters!".formatted(MAX_TOKEN)
            );
        }

    }

    private void validateOutput(ChatClientResponse chatClientResponse) {

        ChatResponse chatResponse = chatClientResponse.chatResponse();

        if (chatResponse == null) {
            throw new RuntimeException("LLM returned a null result!");
        }

        String output = chatResponse.getResult().getOutput().getText();

        if (StringUtils.isEmpty(output)) {
            throw new RuntimeException("LLM returned a empty response!");
        }

    }

}
