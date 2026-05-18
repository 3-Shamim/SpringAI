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
@RequestMapping(value = "/api/chats")
public class Controller {

    private final ChatClient geminiChatClient;
    private final ChatClient openAIChatClient;

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


    @PostMapping(value = "/openai")
    public ResponseEntity<?> chatWithOpenAI(@RequestParam String msg) {

        String response = openAIChatClient
                .prompt()
                .user(msg)
                .call()
                .content();

        log.info("Response from OpenAI: {}", response);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping(value = "/openai/summarize")
    public ResponseEntity<?> summarizeWithOpenAI(@RequestParam String msg) {

        String system = """
                You just only give answer related to sports. For other topics just tell the user this is outside your
                context.
                """;

        String response = openAIChatClient
                .prompt()
                .system(system)
                .user(msg)
                .call()
                .content();

        log.info("Response from OpenAI: {}", response);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping(value = "/openai/sports-man/details")
    public ResponseEntity<?> formatResponseWithOpenAI(@RequestParam String name) {

        String system = """
                You will only give answer related to sports. For other topics just tell the user this is outside of
                your context.
                """;

        Message systemMsg = SystemPromptTemplate.builder()
                .template(system)
                .build()
                .createMessage();

        String msg = "Give summary of the sports for {name} and only 10 of his/her achievements.";

        Message userMsg = PromptTemplate
                .builder()
                .template(msg)
                .variables(Map.of("name", name))
                .build()
                .createMessage();

        SportResponse response = openAIChatClient
                .prompt(new Prompt(systemMsg, userMsg))
                .call()
                .entity(SportResponse.class);

        log.info("Response from OpenAI: {}", response);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


}
