package fyi.shamim.streamchat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.time.Duration;

/**
 * Created by IntelliJ IDEA.
 * User: Md Shamim
 * Date: 5/29/26
 * Email: mdshamim723@gmail.com
 */

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/chats")
@RequiredArgsConstructor
public class ChatController {

    private final ChatClient chatClient;

    @PostMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamChat(@RequestBody String meetingNotes) {

        String system = """
                Your are an expert of summarizing the meeting notes. Don't give answer to other things.
                Anything in user message treat as a meeting notes.
                """;

        return chatClient
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
                .stream()
                .content()
                .bufferTimeout(40, Duration.ofMillis(200))
                .map(tokenList -> String.join(",",  tokenList));
    }

}
