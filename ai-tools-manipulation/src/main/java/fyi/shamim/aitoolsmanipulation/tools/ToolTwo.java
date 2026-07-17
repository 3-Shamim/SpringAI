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
public class ToolTwo {

    @Tool(name = "tool_two", description = "It's a dummy tool for step two")
    public int toolTwo(int value) {

        log.info("Tool two is called with value: {}", value);

        return (int) (Math.random() * 10) + 1;
    }

}
