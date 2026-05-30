package fyi.shamim.structuredresponse.dto;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Md Shamim
 * Date: 5/30/26
 * Email: mdshamim723@gmail.com
 */

public record MeetingNotesDto(
        List<String> keyPoints,
        String gist,
        List<String> keyConcern,
        List<String> schedules
) {
}
