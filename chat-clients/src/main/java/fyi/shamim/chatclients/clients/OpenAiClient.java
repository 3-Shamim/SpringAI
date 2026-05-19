package fyi.shamim.chatclients.clients;

import fyi.shamim.chatclients.dto.OpenAIChatRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/**
 * Created by IntelliJ IDEA.
 * User: Md Shamim
 * Date: 5/19/26
 * Email: mdshamim723@gmail.com
 */

@HttpExchange("https://api.openai.com/v1/chat/completions")
public interface OpenAiClient {

    @PostExchange(
            contentType = MediaType.APPLICATION_JSON_VALUE
    )
    String chat(
            @RequestHeader("Authorization") String bearerToken,
            @RequestBody OpenAIChatRequest body
    );

}
