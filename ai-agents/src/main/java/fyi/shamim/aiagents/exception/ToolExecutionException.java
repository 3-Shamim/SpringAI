package fyi.shamim.aiagents.exception;

import lombok.Getter;

/**
 * Created by IntelliJ IDEA.
 * User: Md Shamim
 * Date: 6/30/26
 * Email: mdshamim723@gmail.com
 */

@Getter
public class ToolExecutionException extends RuntimeException {

    private final String toolName;
    private final String errorCode;
    private final String detail;

    public ToolExecutionException(String toolName, String errorCode, String detail) {
        super("%s failed: %s - %s".formatted(toolName, errorCode, detail));
        this.toolName = toolName;
        this.errorCode = errorCode;
        this.detail = detail;
    }

}