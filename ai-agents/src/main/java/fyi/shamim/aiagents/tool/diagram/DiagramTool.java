package fyi.shamim.aiagents.tool.diagram;

import fyi.shamim.aiagents.config.AiAgentConfigData;
import fyi.shamim.aiagents.service.FileStorageService;
import fyi.shamim.aiagents.tool.diagram.record.DiagramExtractResult;
import fyi.shamim.aiagents.tool.diagram.record.ExtractArgs;
import fyi.shamim.aiagents.tool.diagram.record.Node;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.ResponseFormat;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MimeType;

import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Md Shamim
 * Date: 6/25/26
 * Email: mdshamim723@gmail.com
 */

@Slf4j
@Component
public class DiagramTool {

    private static final String SYSTEM_PROMPT = """
            You are a vision parser that extracts a software architecture graph from a diagram image.
            Output STRICT JSON ONLY (no prose). Use this shape:
            {
              "nodes": [{ "id": "string", "type": "string", "zone": "string", "technology": "string", "labels": ["string"], "metadata": { "key": "value" } }],
              "edges": [{ "from": "string", "to": "string", "protocol": "string", "port": 0, "auth": "string", "encrypted": true, "notes": "string" }],
              "dataStores": [{ "id": "string", "type": "string", "classification": "string", "encryptedAtRest": true }],
              "trustBoundaries": [{ "name": "string", "includes": ["string"] }]
            }
            Rules:
            - Use stable and machine-friendly IDs (kebab or snake case).
            - Infer protocol/auth/encription if clearly shown (TLS lock, HTTPS, mTLS notes).
            - Prefer concise fields; omit unknowns with empty string or null.
            - Do NOT add commentary. JSON only.
            """;

    private final ChatClient chatClient;
    private final FileStorageService fileStorageService;
    private final AiAgentConfigData.DiagramToolProperties diagramToolProperties;

    public DiagramTool(@Qualifier("chatClientForDiagram")
                        ChatClient chatClient,
                       FileStorageService fileStorageService,
                       AiAgentConfigData aiAgentConfigData) {

        this.chatClient = chatClient;
        this.fileStorageService = fileStorageService;
        this.diagramToolProperties = aiAgentConfigData.getDiagramTool();
    }

    @Tool(
            name = "diagram_extract",
            description = "Extract components/edges from an uploaded architecture diagram (image, Draw.io PNG export or screenshot)"
    )
    public Map<String, Object> extractDiagram(ExtractArgs args) {

        try {

            log.info("Calling diagram_extract tool with file: {} and id: {}", args.fileName(), args.id());

            var path = fileStorageService.resolvePath(args.fileName());
            var resource = new FileSystemResource(path);
            var mime = Files.probeContentType(path);
            var userText = buildUserText(args.hints());
            var chatOptions = getChatOptions();
            DiagramExtractResult result = doDiagramExtract(args.id(), userText, mime, resource, chatOptions);

            return toMap(result);
        } catch (IOException e) {
            log.error("Error in diagram parse tool", e);
            return Map.of(
                    "error", "DIAGRAM_PARSE_FAILED",
                    "message", e.getMessage()
            );
        }

    }

    private String buildUserText(List<String> hints) {

        String base = "Extract nodes, edges, data stores, message brokers(kafka), event-driven communications and trust boundaries.";

        if (CollectionUtils.isEmpty(hints)) {
            return base;
        }

        return String.format("%s Hints: %s", base, String.join(",", hints));
    }

    private ChatOptions getChatOptions() {

        return OpenAiChatOptions.builder()
                .temperature(diagramToolProperties.getTemperature())
                .responseFormat(
                        ResponseFormat.builder()
                                .type(ResponseFormat.Type.JSON_OBJECT)
                                .build()
                )
                .build();
    }

    private DiagramExtractResult doDiagramExtract(String id, String userText, String mime,
                                                  FileSystemResource resource,
                                                  ChatOptions chatOptions) {

        return chatClient.prompt()
                .system(SYSTEM_PROMPT)
                .user(userSpec -> {
                    userSpec.text(userText);
                    userSpec.media(MimeType.valueOf(mime), resource);
                })
                .options(chatOptions)
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, id))
                .call()
                .entity(DiagramExtractResult.class);
    }

    private Map<String, Object> toMap(DiagramExtractResult diagramExtractResult) {

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("nodes", normalizeServiceIds(diagramExtractResult.nodes()));
        map.put("edges", diagramExtractResult.edges());
        map.put("dataStores", diagramExtractResult.dataStores());
        map.put("trustBoundaries", diagramExtractResult.trustBoundaries());

        return map;
    }

    private List<Node> normalizeServiceIds(List<Node> nodes) {

        List<Node> normalizedNodes = new ArrayList<>();

        // track canonical service ids we've added so we can skip
        // duplicates like "Order Service-1", "Order Service-2" or "order_service_1"
        Set<String> seen = new LinkedHashSet<>();

        if (nodes == null) {
            return normalizedNodes;
        }

        for (var node : nodes) {

            if (node == null) continue;

            // compute a canonical id for the node; if it looks like a service id (endsWith -service)
            // then treat duplicates as the same entity regardless of original separators or numbering

            String canonical = toKebabService(node.id());

            if (canonical != null && canonical.endsWith("-service")) {
                if (seen.contains(canonical)) {
                    // skip duplicate numbered/underscored service
                    continue;
                }
                seen.add(canonical);
                normalizedNodes.add(node.withId(canonical));
            } else {
                // non-service or non-canonicalizable nodes are preserved as-is
                normalizedNodes.add(node);
            }

        }

        return normalizedNodes;
    }

    private String toKebabService(String id) {

        if (id == null) {
            return null;
        }

        String serviceId = id.trim();
        // replace whitespace with dash first
        serviceId = serviceId.replaceAll("\\s+", "-");
        // snake_case -> kebab-case
        serviceId = serviceId.replace('_', '-');
        // drop double dashes
        serviceId = serviceId.replaceAll("-{2,}", "-");
        // strip trailing numeric suffix like -1, -2 which are diagram instance markers
        serviceId = serviceId.replaceAll("-\\d+$", "");
        // strip common suffixes
        serviceId = serviceId.replaceAll("(?i)-(svc|service)$", "");
        // lowercase
        serviceId = serviceId.toLowerCase(Locale.ROOT);

        // append "-service" if it looks like a service id (not db/client)
        if (!serviceId.endsWith("-service")
                && !serviceId.endsWith("-api")
                && !serviceId.endsWith("-db")
                && !serviceId.endsWith("-client")) {

            serviceId = serviceId + "-service";
        }

        return serviceId;
    }

}
