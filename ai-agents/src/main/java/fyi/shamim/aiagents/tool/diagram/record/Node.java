package fyi.shamim.aiagents.tool.diagram.record;

import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Md Shamim
 * Date: 6/25/26
 * Email: mdshamim723@gmail.com
 */

public record Node(
        String id,
        String type,
        String zone,
        String technology,
        List<String> labels,
        Map<String, Object> metadata
) {

    public Node withId(String newId) {
        return new Node(
                newId,
                this.type,
                this.zone,
                this.technology,
                this.labels,
                this.metadata
        );
    }

}
