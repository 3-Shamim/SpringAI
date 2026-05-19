package fyi.shamim.chatclients.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Md Shamim
 * Date: 5/19/26
 * Email: mdshamim723@gmail.com
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public record OpenAIChatRequest(
        String model,
        List<OpenAIChatMessage> messages
) {

    public record OpenAIChatMessage(
            Role role,
            String content
    ) {

    }

    public enum Role {
        user, developer
    }

}
