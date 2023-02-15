package CoffeeOrderWebApp.practice.exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

@Slf4j
//@RestControllerAdvice
public class ControllerAdvice {
    @ExceptionHandler(LogicException.class)
    public ResponseEntity logicExceptionHandler(LogicException e){
        log.error("# LogicException : {}", e.getExceptionEnum().getMessage());
        return new ResponseEntity<>(e.getExceptionEnum().getMessage(), e.getExceptionEnum().getHttpStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e){
        log.error("# MethodArgumentNotValidException : {}", e.getMessage());
        return new ResponseEntity<>(ErrorResponse.of(e.getBindingResult()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity constraintViolationExceptionHandler(ConstraintViolationException e){
        log.error("# ConstraintViolationException : {}", e.getMessage());
        return new ResponseEntity<>(ErrorResponse.of(e.getConstraintViolations()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity httpRequestMethodNotSupportedExceptionHandler(HttpRequestMethodNotSupportedException e){
        log.error("# HttpRequestMethodNotSupportedException : {}", e.getMessage());
        return new ResponseEntity<>(ErrorResponse.of(e), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity exceptionHandler(Exception e){
        log.error("# Exception : {}", e.getMessage());
        return new ResponseEntity<>(ErrorResponse.of(e), HttpStatus.BAD_REQUEST);
    }
}
