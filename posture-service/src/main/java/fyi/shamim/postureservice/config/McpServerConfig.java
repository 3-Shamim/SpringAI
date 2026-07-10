package fyi.shamim.postureservice.config;

import fyi.shamim.postureservice.tool.PostureTool;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by IntelliJ IDEA.
 * User: Md Shamim
 * Date: 7/9/26
 * Email: mdshamim723@gmail.com
 */

@Configuration
public class McpServerConfig {

    @Bean
    public ToolCallbackProvider toolCallbackProvider(PostureTool postureTool) {

        return MethodToolCallbackProvider.builder()
                .toolObjects(postureTool)
                .build();
    }

}
