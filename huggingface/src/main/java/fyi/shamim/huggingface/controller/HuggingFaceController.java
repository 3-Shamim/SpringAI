package fyi.shamim.huggingface.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by IntelliJ IDEA.
 * User: Md Shamim
 * Date: 5/23/26
 * Email: mdshamim723@gmail.com
 */

@Tag(name = "HuggingFace Chat", description = "Chat via HuggingFace Inference API shared models")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chats")
public class HuggingFaceController {

    private final ChatClient openAiChatClient;
    private final ChatClient huggingFaceChatClient;

    @Operation(
            summary = "Send a message to HuggingFace model",
            description = "Sends a user message to a shared HuggingFace Inference API model and returns the response."
    )
    @PostMapping("/shared-huggingface-using-openai")
    public ResponseEntity<?> chatWithSharedHuggingFaceUsingOpenAi(
            @Parameter(description = "User message to send to the model", required = true)
            @RequestParam String msg) {

        ChatClientResponse response = openAiChatClient
                .prompt()
                .user(msg)
                .call()
                .chatClientResponse();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @Operation(
            summary = "Send a message to HuggingFace model",
            description = "Sends a user message to a shared HuggingFace Inference API model and returns the response."
    )
    @PostMapping("/huggingface")
    public ResponseEntity<?> chat(
            @Parameter(description = "User message to send to the model", required = true)
            @RequestParam String msg) {

        ChatClientResponse response = huggingFaceChatClient
                .prompt()
                .user(msg)
                .call()
                .chatClientResponse();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
