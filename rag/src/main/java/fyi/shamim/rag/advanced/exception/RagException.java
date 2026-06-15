package fyi.shamim.rag.advanced.exception;

/**
 * Created by IntelliJ IDEA.
 * User: Md Shamim
 * Date: 6/14/26
 * Email: mdshamim723@gmail.com
 */

public class RagException extends RuntimeException {

    public RagException(String message) {
        super(message);
    }

    public RagException(String message, Throwable cause) {
        super(message, cause);
    }

}
