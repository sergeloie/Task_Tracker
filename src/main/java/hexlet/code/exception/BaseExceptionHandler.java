package hexlet.code.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.hibernate.exception.ConstraintViolationException;
import org.postgresql.util.PSQLException;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.sql.SQLException;
import java.util.NoSuchElementException;

@DependsOn("labelInitializer")
@ResponseBody
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class BaseExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String handleException(Exception exception) {
        return exception.getMessage();
    }

    @ExceptionHandler(RuntimeException.class)
    public String handleException(RuntimeException exception) {
        return exception.getMessage();
    }

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

    @ExceptionHandler(SQLException.class)
    public String handleSQLException(SQLException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public String handleConstraintViolationException(ConstraintViolationException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler(PSQLException.class)
    public String handlePSQLException(PSQLException exception) {
        return exception.getMessage();
    }


}
