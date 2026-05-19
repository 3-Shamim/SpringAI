package fyi.shamim.chatclients.controller;

import fyi.shamim.chatclients.dto.SportResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Md Shamim
 * Date: 5/16/26
 * Email: mdshamim723@gmail.com
 */

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/chats/gemini")
public class GeminiController {

    private final ChatClient geminiChatClient;

    @PostMapping(value = "/gemini")
    public ResponseEntity<?> chatWithGemini(@RequestParam String msg) {

        String response = geminiChatClient
                .prompt()
                .user(msg)
                .call()
                .content();
        log.info("Response from Gemini: {}", response);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
