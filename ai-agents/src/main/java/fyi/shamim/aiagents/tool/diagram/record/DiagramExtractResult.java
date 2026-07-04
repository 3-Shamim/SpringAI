package fyi.shamim.aiagents.tool.diagram.record;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Md Shamim
 * Date: 6/25/26
 * Email: mdshamim723@gmail.com
 */

public record DiagramExtractResult(
        List<Node> nodes,
        List<Edge> edges,
        List<DataStore> dataStores,
        List<TrustBoundary> trustBoundaries
) {
}
