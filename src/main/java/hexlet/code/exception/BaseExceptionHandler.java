package hexlet.code.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.NoSuchElementException;

@ResponseBody
@ControllerAdvice

public class BaseExceptionHandler {

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public String dataIntegrityViolationExceptionHandler(DataIntegrityViolationException exception) {
        return exception.getCause().getCause().getMessage();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    public String noSuchElementExceptionHandler(NoSuchElementException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler(JsonProcessingException.class)
    public String jsonProcessingExceptionHandler(JsonProcessingException exception) {
        return exception.getCause().getCause().getMessage();
    }

    @ExceptionHandler(DataAccessException.class)
    public String dataAccessExceptionHandler(DataAccessException exception) {
        return exception.getCause().getCause().getMessage();
    }


    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public String resourceNotFoundExceptionHandler(ResourceNotFoundException exception) {
        return exception.getMessage();
    }
}
