package fyi.shamim.advisor.exception;

import fyi.shamim.advisor.dto.ErrorDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Created by IntelliJ IDEA.
 * User: Md Shamim
 * Date: 5/31/26
 * Email: mdshamim723@gmail.com
 */

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(exception = Exception.class)
    public ErrorDto handleException(Exception e) {
        log.error("Exception:", e);
        return new ErrorDto(e.getMessage());
    }

}
