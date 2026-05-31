package fyi.shamim.chatoptions.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.ollama.api.OllamaChatOptions;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Md Shamim
 * Date: 5/30/26
 * Email: mdshamim723@gmail.com
 */

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/chats")
@RequiredArgsConstructor
public class ChatController {

    private final ChatClient openAiChatClient;
    private final ChatClient ollamaChatClient;

    @PostMapping("/openai")
    public ResponseEntity<?> chatWithOpenAI(@RequestBody String text) {

        String response = openAiChatClient
                .prompt()
                .user(text)
                .call()
                .content();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/openai-custom")
    public ResponseEntity<?> chatWithOpenAICustomOption(@RequestBody String text) {

        ChatOptions options = ChatOptions.builder()
                .model(OpenAiApi.ChatModel.GPT_5_CHAT_LATEST.getName())
                .temperature(0.7)
                .maxTokens(100)
//                .topK() // OpenAI doesn't support it.
                .topP(0.7)
                .stopSequences(List.of("END"))
                .build();

        String response = openAiChatClient
                .prompt()
                .options(options)
                .user(text)
                .call()
                .content();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @PostMapping("/ollama")
    public ResponseEntity<?> chatWithOllama(@RequestBody String text) {

        String response = ollamaChatClient
                .prompt()
                .user(text)
                .call()
                .content();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/ollama-custom")
    public ResponseEntity<?> chatWithOllamaCustomOption(@RequestBody String text) {

        OllamaChatOptions options = OllamaChatOptions.builder()
                .model("mistral-email-draft")
                .temperature(0.7)
                .build();

        String response = ollamaChatClient
                .prompt()
                .options(options)
                .user(text)
                .call()
                .content();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


}
