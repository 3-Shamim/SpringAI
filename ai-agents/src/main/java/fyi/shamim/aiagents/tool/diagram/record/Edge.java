package fyi.shamim.aiagents.tool.diagram.record;

/**
 * Created by IntelliJ IDEA.
 * User: Md Shamim
 * Date: 6/25/26
 * Email: mdshamim723@gmail.com
 */

public record Edge(
        String from,
        String to,
        String protocol,
        Integer port,
        String auth,
        Boolean encrypted,
        String notes
) {
}
