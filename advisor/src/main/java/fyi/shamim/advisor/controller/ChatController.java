package fyi.shamim.advisor.controller;

import fyi.shamim.advisor.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping
    public ResponseEntity<?> chatWithOpenAI(@RequestBody String text) {

        ResponseDto response = openAiChatClient
                .prompt()
                .user(text)
                .call()
                .entity(ResponseDto.class);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
