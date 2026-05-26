package fyi.shamim.localollamaintegration.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.ollama.api.OllamaChatOptions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by IntelliJ IDEA.
 * User: Md Shamim
 * Date: 5/25/26
 * Email: mdshamim723@gmail.com
 */

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/chats")
@RequiredArgsConstructor
public class ChatController {

    private final ChatClient openAiChatClient;
    private final ChatClient ollamaChatClient;

    @PostMapping
    public ResponseEntity<?> chatWithOllama(@RequestBody String message) {

        ChatClientResponse response = ollamaChatClient
                .prompt()
                .user(message)
                .call()
                .chatClientResponse();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/open-ai-client")
    public ResponseEntity<?> chatWithOllamaUsingOpenAiClient(@RequestBody String message) {

        ChatClientResponse response = openAiChatClient
                .prompt()
                .user(message)
                .call()
                .chatClientResponse();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/email-draft")
    public ResponseEntity<?> draftEmail(@RequestBody String message) {

        String systemPrompt = """
                You are a helpful assistance that drafts personal and concise emails based on user input.
                Ensure the emails are clear, polite, and tailored to the specific context.
                Use a format and respectful tone while maintaining brevity.
                """;

        String response = ollamaChatClient
                .prompt()
                .system(systemPrompt)
                .user(message)
                .call()
                .content();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/email-draft/custom-model")
    public ResponseEntity<?> draftEmailWithCustomModel(@RequestBody String message) {

        OllamaChatOptions.Builder options = OllamaChatOptions.builder()
                .model("mistral-email-draft:latest");
        // Temperature and Max token already set in the custom model
        // System prompt has been set as well

        String response = ollamaChatClient
                .prompt()
                .options(options)
                .user(message)
                .call()
                .content();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
