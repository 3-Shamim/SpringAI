package fyi.shamim.aitoolsmanipulation.tools;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

/**
 * Created by IntelliJ IDEA.
 * User: Md Shamim
 * Date: 7/5/26
 * Email: mdshamim723@gmail.com
 */

@Slf4j
@Component
public class ToolThree {

    @Tool(name = "tool_three", description = "It's a dummy tool for step three")
    public int toolThree(int value) {

        log.info("Tool three is called with value: {}", value);

        return (int) (Math.random() * 10) + 1;
    }

}
