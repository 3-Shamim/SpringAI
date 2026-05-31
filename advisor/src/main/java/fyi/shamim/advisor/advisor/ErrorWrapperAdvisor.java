package fyi.shamim.advisor.advisor;

import fyi.shamim.advisor.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisor;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisorChain;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Md Shamim
 * Date: 5/31/26
 * Email: mdshamim723@gmail.com
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class ErrorWrapperAdvisor implements CallAdvisor, StreamAdvisor {

    private final ObjectMapper objectMapper;

    @Override
    public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain callAdvisorChain) {

        log.info(
                "Request received in ErrorWrapperAdvisor with prompt: {}",
                chatClientRequest.prompt().getUserMessage().getText()
        );

        ChatClientResponse chatClientResponse = callAdvisorChain.nextCall(chatClientRequest);

        String assistanceMsg = chatClientResponse.chatResponse().getResult().getOutput().getText();

        if (!assistanceMsg.startsWith("```json") && !assistanceMsg.startsWith("{")) {

            ResponseDto responseDto = new ResponseDto(null, assistanceMsg);

//            chatClientResponse = new ChatClientResponse(
//                    new ChatResponse(List.of(new Generation(new AssistantMessage(objectMapper.writeValueAsString(responseDto))))),
//                    Map.copyOf(chatClientRequest.context())
//            );

            chatClientResponse = chatClientResponse.mutate()
                    .chatResponse(
                            new ChatResponse(List.of(
                                    new Generation(new AssistantMessage(objectMapper.writeValueAsString(responseDto)))
                            ))
                    )
                    .context(Map.copyOf(chatClientRequest.context()))
                    .build();

        }

        return chatClientResponse;
    }

    @Override
    public Flux<ChatClientResponse> adviseStream(ChatClientRequest chatClientRequest, StreamAdvisorChain streamAdvisorChain) {

        return streamAdvisorChain.nextStream(chatClientRequest);
    }

    @Override
    public String getName() {
        return "ErrorWrapperAdvisor";
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

}
