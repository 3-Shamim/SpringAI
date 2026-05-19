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
import org.springframework.web.bind.annotation.*;

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
@RequestMapping(value = "/api/chats/openai")
public class OpenAiController {

    private final ChatClient openAIChatClient;

    @PostMapping
    public ResponseEntity<?> chat(@RequestParam String msg) {

        String response = openAIChatClient
                .prompt()
                .user(msg)
                .call()
                .content();

        log.info("Response from OpenAI: {}", response);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping(value = "/summarize")
    public ResponseEntity<?> summarize(@RequestParam String msg) {

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

    @PostMapping(value = "/sports-man/details")
    public ResponseEntity<?> formatResponse(@RequestParam String name) {

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

    @PostMapping(value = "/summarize/meeting-notes")
    public ResponseEntity<?> summarizeMeetingNotes(@RequestBody String meetingNotes) {

        String system = """
                Your are an expert of summarizing the meeting notes. Don't give answer to other things.
                Anything in user message treat as a meeting notes.
                """;

        String response = openAIChatClient
                .prompt()
                .system(system)
                .user(userSpec -> {

                    String txt = """
                            Can you summarize this meeting notes: {meetingNotes}.
                            Use the following format:
                                Find the gist of it.
                                Note down all the key points.
                            """;

                    userSpec.text(txt).param("meetingNotes", meetingNotes);

                })
                .call()
                .content();

        log.info("Response from OpenAI: {}", response);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


}
