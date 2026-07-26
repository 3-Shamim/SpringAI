package fyi.shamim.mcpserver.tool;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

/**
 * Created by IntelliJ IDEA.
 * User: Md Shamim
 * Date: 7/26/26
 * Email: mdshamim723@gmail.com
 */

@Slf4j
@Component
public class ArithmeticTool {

    @Tool(name = "sum", description = "This tool will return the sum of two value.")
    public int sum(int a, int b) {
        log.info("We are summing {} with {}", a, b);
        return a + b;
    }

    @Tool(name = "minus", description = "This tool will return the substruct of two value.")
    public int minus(int a, int b) {
        log.info("We are substructing {} with {}", a, b);
        return a - b;
    }

}
