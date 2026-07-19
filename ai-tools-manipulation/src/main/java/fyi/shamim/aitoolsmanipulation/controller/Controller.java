package fyi.shamim.aitoolsmanipulation.controller;

import fyi.shamim.aitoolsmanipulation.tools.ToolOne;
import fyi.shamim.aitoolsmanipulation.tools.ToolThree;
import fyi.shamim.aitoolsmanipulation.tools.ToolTwo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.AdvisorParams;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.tool.ToolCallingChatOptions;
import org.springframework.ai.model.tool.ToolCallingManager;
import org.springframework.ai.model.tool.ToolExecutionResult;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Md Shamim
 * Date: 7/15/26
 * Email: mdshamim723@gmail.com
 */

@Slf4j
@RestController
@RequestMapping("/api/v1/tools")
@RequiredArgsConstructor
public class Controller {

    private final ChatClient chatClient;
    private final ToolCallingManager toolCallingManager;
    private final ToolOne toolOne;
    private final ToolTwo toolTwo;
    private final ToolThree toolThree;

    @PostMapping
    public ResponseEntity<?> toolCalling() {

        String user = """
                Tool executor. Don't miss the steps and order.
                """;

        String system = """
                You are a tool caller AI. Your work is to call all tools with following order:
                tool_one -> tool_two -> tool_three.
                Pass the previous tool result to the the next one except for the first one.
                Don't break the order.
                Give the individual tool result.
                """;

        String content = chatClient.prompt()
                .user(user)
                .system(system)
                .tools(toolOne, toolTwo, toolThree)
                .call()
                .content();

        return ResponseEntity.status(HttpStatus.OK).body(content);
    }

    @PostMapping("/auto")
    public ResponseEntity<?> autoToolCalling() {

        String user = """
                Tool executor. Don't miss the steps and order.
                """;

        String system = """
                You are a tool caller AI. Your work is to call all tools with following order:
                tool_one -> tool_two -> tool_three.
                Pass the previous tool result to the the next one except for the first one.
                Don't break the order.
                Give the individual tool result.
                """;

        Prompt prompt = new Prompt(
                List.of(
                        UserMessage.builder().text(user).build(),
                        SystemMessage.builder().text(system).build()
                ),
                ToolCallingChatOptions.builder()
                        .toolCallbacks(ToolCallbacks.from(toolOne, toolTwo, toolThree))
                        .build()
        );

        String content = chatClient.prompt(prompt)
                .call()
                .content();

        return ResponseEntity.status(HttpStatus.OK).body(content);
    }


    @PostMapping("/manual")
    public ResponseEntity<?> manualToolCalling() {

        String user = """
                Tool executor. Don't miss the steps and order.
                """;

        String system = """
                You are a tool caller AI. Your work is to call all tools with following order:
                tool_one -> tool_two -> tool_three.
                Pass the previous tool result to the the next one except for the first one.
                Don't break the order. Call a tool once.
                Give the individual tool result.
                """;

        ToolCallingChatOptions toolCallingChatOptions = ToolCallingChatOptions.builder()
                .toolCallbacks(ToolCallbacks.from(toolOne, toolTwo, toolThree))
                .build();

        Prompt prompt = new Prompt(
                List.of(
                        UserMessage.builder().text(user).build(),
                        SystemMessage.builder().text(system).build()
                ),
                toolCallingChatOptions
        );

        ChatResponse chatResponse = chatClient.prompt(prompt)
                .advisors(AdvisorParams.toolCallingAdvisorAutoRegister(false))
                .call()
                .chatResponse();

        if (chatResponse == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No chat response");
        }

        while (chatResponse.hasToolCalls()) {

            ToolExecutionResult toolExecutionResult = toolCallingManager.executeToolCalls(prompt, chatResponse);
            log.info("Execute tool: {}", toolExecutionResult.conversationHistory());
            prompt = new Prompt(toolExecutionResult.conversationHistory(), toolCallingChatOptions);

            chatResponse = chatClient.prompt(prompt)
                    .advisors(AdvisorParams.toolCallingAdvisorAutoRegister(false))
                    .call()
                    .chatResponse();

            if (chatResponse == null) {
                break;
            }

        }

        String content = chatClient.prompt(prompt)
                .call()
                .content();

        return ResponseEntity.status(HttpStatus.OK).body(content);
    }

}
