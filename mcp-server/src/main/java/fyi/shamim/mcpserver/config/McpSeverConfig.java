package fyi.shamim.mcpserver.config;

import fyi.shamim.mcpserver.tool.ArithmeticTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by IntelliJ IDEA.
 * User: Md Shamim
 * Date: 7/26/26
 * Email: mdshamim723@gmail.com
 */

@Slf4j
@Configuration
public class McpSeverConfig {

    @Bean
    public ToolCallbackProvider toolCallbackProvider(ArithmeticTool arithmeticTool) {

        return MethodToolCallbackProvider.builder()
                .toolObjects(arithmeticTool)
                .build();
    }

}
