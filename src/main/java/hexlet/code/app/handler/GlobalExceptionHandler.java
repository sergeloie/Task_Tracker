package hexlet.code.app.handler;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
//    @ExceptionHandler(DataIntegrityViolationException.class)
//    public String handleDataIntegrityViolationException(DataIntegrityViolationException e) {
//        return ("Запись с такими данными уже существует" + e.getMessage());
//    }

    @ExceptionHandler(org.hibernate.exception.ConstraintViolationException.class)
    public String handleConstraintViolationException(org.hibernate.exception.ConstraintViolationException e) {
        return ("Запись с такими данными уже существует" + e.getMessage());
    }


    public ResponseEntity<String> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        // Обработка исключения дублирования записи
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Запись с такими данными уже существует");
    }
}
