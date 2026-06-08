package fyi.shamim.chatmemorywithspringaiv1.controller;

import fyi.shamim.chatmemorywithspringaiv1.tools.OrderStatusTools;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by IntelliJ IDEA.
 * User: Md Shamim
 * Date: 6/6/26
 * Email: mdshamim723@gmail.com
 */

@Slf4j
@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final ChatClient chatClientWithCustomChatMemory;
    private final ChatMemory customChatMemory;
    private final OrderStatusTools orderStatusTools;

    @PostMapping("/status")
    public ResponseEntity<?> orderStatus(@RequestBody String message, @RequestHeader String userId) {

        customChatMemory.get(userId).forEach(m -> log.info("UserID: {}, Message: {}", userId, m));

        String systemMsg = """
                You are a very special order tracker assistant. You only help user to track their order status.
                - Note:
                --- If user provides order ID or you find order ID in chat memory, then use tools to get the order status.
                --- Our tools:
                ----- get-order-status
                - Guide:
                -- Don't drive order status by your self.
                -- Keep responses short and clean.
                """;

        String userMsg = "%s. User ID: %s".formatted(message, userId);

        String chatResponse = chatClientWithCustomChatMemory.prompt()
                .system(systemMsg)
                .user(userMsg)
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, userId))
                .tools(orderStatusTools)
                .call()
                .content();

        return ResponseEntity.status(HttpStatus.OK).body(chatResponse);
    }

}
