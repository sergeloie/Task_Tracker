package hexlet.code.exception;

import org.springframework.core.NestedRuntimeException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Objects;

@ResponseBody
@ControllerAdvice
public class BaseExceptionHandler {

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public String dataIntegrityViolationExceptionHandler(DataIntegrityViolationException exception) {
//        return errorMessage = ((NestedRuntimeException) Objects.requireNonNull(exception.getRootCause()))
//                .getMostSpecificCause()
//                .getMessage();

        return exception.getCause().getCause().getMessage();
    }
}
