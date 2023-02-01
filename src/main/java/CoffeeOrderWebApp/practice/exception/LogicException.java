package CoffeeOrderWebApp.practice.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LogicException extends RuntimeException{
    private ExceptionEnum exceptionEnum;

    public LogicException(ExceptionEnum exceptionEnum) {
        this.exceptionEnum = exceptionEnum;
    }
}
