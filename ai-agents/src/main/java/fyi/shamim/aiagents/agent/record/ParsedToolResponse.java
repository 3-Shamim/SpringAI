package fyi.shamim.aiagents.agent.record;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Md Shamim
 * Date: 6/29/26
 * Email: mdshamim723@gmail.com
 */

public record ParsedToolResponse(Map<String, Object> payload, boolean isError) {
}
