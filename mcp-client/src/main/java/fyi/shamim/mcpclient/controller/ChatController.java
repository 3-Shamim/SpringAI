package fyi.shamim.mcpclient.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by IntelliJ IDEA.
 * User: Md Shamim
 * Date: 7/26/26
 * Email: mdshamim723@gmail.com
 */

@Slf4j
@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ToolCallbackProvider toolCallbackProvider;
    private final ChatClient chatClient;

    @GetMapping("/tools")
    public ResponseEntity<?> tools() {

        return ResponseEntity.status(HttpStatus.OK).body(
                toolCallbackProvider.getToolCallbacks()
        );
    }

    @PostMapping
    public ResponseEntity<?> chat(@RequestBody String question) {

        String content = chatClient.prompt()
                .system("You are a very smart arithmetic calculator. Use tools to do the operations.")
                .user(question)
                .call()
                .content();

        return ResponseEntity.status(HttpStatus.OK).body(content);
    }

}
