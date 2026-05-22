package fyi.shamim.vertexai.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by IntelliJ IDEA.
 * User: Md Shamim
 * Date: 5/21/26
 * Email: mdshamim723@gmail.com
 */

@Tag(name = "Vertex AI Chat", description = "Gemini chat via Spring AI")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chats")
public class VertexAiController {

    private final ChatClient vertexAiChatClient;

    @Operation(summary = "Send a message to Gemini", description = "Sends a user message to Vertex AI Gemini and returns the model's response.")
    @PostMapping("/vertex-ai")
    public ResponseEntity<?> vertexAiChat(
            @Parameter(description = "User message to send to Gemini", required = true)
            @RequestParam String msg) {

        String response = vertexAiChatClient
                .prompt()
                .user(msg)
                .call()
                .content();
        log.info("Response from Gemini: {}", response);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
