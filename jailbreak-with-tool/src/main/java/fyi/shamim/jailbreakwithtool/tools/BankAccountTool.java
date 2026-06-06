package fyi.shamim.jailbreakwithtool.tools;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

/**
 * Created by IntelliJ IDEA.
 * User: Md Shamim
 * Date: 6/4/26
 * Email: mdshamim723@gmail.com
 */

@Slf4j
@Component
public class BankAccountTool {

    @Tool(name = "get-current-balance", description = "Retrieve current balance by account ID.")
    public String getCurrentBalance(@ToolParam(description = "Account ID") String accountId) {

        if (accountId.equals("12345")) {
            return "5000.0";
        }

        return "Account not found.";
    }

}
