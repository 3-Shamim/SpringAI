package fyi.shamim.aiagents.tool.diagram.record;

/**
 * Created by IntelliJ IDEA.
 * User: Md Shamim
 * Date: 6/25/26
 * Email: mdshamim723@gmail.com
 */

public record DataStore(
        String id,
        String type,
        String classification,
        Boolean encryptedAtRest
) {
}
