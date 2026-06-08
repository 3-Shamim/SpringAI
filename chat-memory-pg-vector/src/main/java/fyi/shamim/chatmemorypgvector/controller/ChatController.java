package fyi.shamim.chatmemorypgvector.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by IntelliJ IDEA.
 * User: Md Shamim
 * Date: 6/6/26
 * Email: mdshamim723@gmail.com
 */

@Slf4j
@RestController
@RequestMapping("/api/v1/chats")
@RequiredArgsConstructor
public class ChatController {

    private final ChatClient chatClient;
    private final ChatClient chatClientWithMemory;

    @PostMapping("")
    public ResponseEntity<?> chat(@RequestBody String message) {

        ChatResponse chatResponse = chatClient.prompt()
                .user(message)
                .call()
                .chatResponse();

        return ResponseEntity.status(HttpStatus.OK).body(chatResponse);
    }

    @PostMapping("/memory")
    public ResponseEntity<?> chatWithChatMemory(@RequestBody String message) {

        ChatResponse chatResponse = chatClientWithMemory.prompt()
                .user(message)
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, "default"))
                .call()
                .chatResponse();

        return ResponseEntity.status(HttpStatus.OK).body(chatResponse);
    }

}
