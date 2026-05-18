package fyi.shamim.chatclients.dto;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Md Shamim
 * Date: 5/18/26
 * Email: mdshamim723@gmail.com
 */

public record SportResponse(
        String name,
        List<String> achievements
) {
}
