package fyi.shamim.structuredresponse.controller;

import fyi.shamim.structuredresponse.dto.MeetingNotesDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.ai.converter.MapOutputConverter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    private final ChatClient chatClient;

    @PostMapping
    public ResponseEntity<?> chat(@RequestBody String message) {

        String system = """
                Your are an expert of summarizing the meeting notes. Don't give answer to other things.
                Anything in user message treat as a meeting notes.
                """;


        try {

            MeetingNotesDto meetingNotes = chatClient
                    .prompt()
                    .system(system)
                    .user(userSpec -> {

                        String txt = """
                                Can you summarize this meeting notes: {meetingNotes}.
                                Use the following format:
                                    Find the gist of it.
                                    Note down all the key points.
                                """;

                        userSpec.text(txt).param("meetingNotes", message);

                    }).call()
                    .entity(MeetingNotesDto.class);

            return ResponseEntity.status(HttpStatus.OK).body(meetingNotes);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    @PostMapping(value = "list")
    public ResponseEntity<?> chatReturnList(@RequestBody String message) {

        String system = """
                Your are an expert of summarizing the meeting notes. Don't give answer to other things.
                Anything in user message treat as a meeting notes.
                """;

        List<MeetingNotesDto> meetingNotes;

        try {

            meetingNotes = chatClient
                    .prompt()
                    .system(system)
                    .user(userSpec -> {

                        String txt = """
                                Can you summarize this meeting notes: {meetingNotes}.
                                Give me 3 different example of it.
                                Use the following format:
                                    Find the gist of it.
                                    Note down all the key points.
                                """;

                        userSpec.text(txt).param("meetingNotes", message);

                    }).call()
                    .entity(new ParameterizedTypeReference<>() {
                    });
        } catch (Exception e) {
            meetingNotes = new ArrayList<>();
        }

        return ResponseEntity.status(HttpStatus.OK).body(meetingNotes);
    }

    @PostMapping(value = "/msg/list")
    public ResponseEntity<?> responseList(@RequestBody String message) {

        List<String> list = chatClient
                .prompt()
                .user(message)
                .call()
                .entity(new ListOutputConverter());

        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    @PostMapping(value = "/msg/map")
    public ResponseEntity<?> responseMap(@RequestBody String message) {

        Map<String, Object> list = chatClient
                .prompt()
                .user(message)
                .call()
                .entity(new MapOutputConverter());

        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

}
