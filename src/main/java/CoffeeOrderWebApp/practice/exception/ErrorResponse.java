package CoffeeOrderWebApp.practice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class ErrorResponse {
    private String message;
    private HttpStatus status;
    private List<FieldError> errors;
    private List<ConstraintViolation> constraintViolations;

    public ErrorResponse(List<FieldError> errors, List<ConstraintViolation> constraintViolations) {
        this.errors = errors;
        this.constraintViolations = constraintViolations;
    }

    public ErrorResponse(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

    public static ErrorResponse of(BindingResult bindingResult) {
        return new ErrorResponse(FieldError.of(bindingResult), null);
    }

    public static ErrorResponse of(Set<javax.validation.ConstraintViolation<?>> constraintViolation) {
        return new ErrorResponse(null, ConstraintViolation.of(constraintViolation));
    }

    public static ErrorResponse of(HttpRequestMethodNotSupportedException e) {
        return new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    public static ErrorResponse of(Exception e) {
        return new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @Getter
    private static class FieldError {
        private String field;
        private String rejectedValue;
        private String message;

        private FieldError(String field, String rejectedValue, String message) {
            this.field = field;
            this.rejectedValue = rejectedValue;
            this.message = message;
        }

        public static List<FieldError> of(BindingResult bindingResult) {
            List<org.springframework.validation.FieldError> fieldErrors = bindingResult.getFieldErrors();
            return fieldErrors.stream().map(
                    fieldError ->
                            new FieldError(
                                    fieldError.getField(),
                                    fieldError.getRejectedValue() == null ? "" : fieldError.getRejectedValue().toString(),
                                    fieldError.getDefaultMessage()
                            )
            ).collect(Collectors.toList());
        }
    }

    @Getter
    private static class ConstraintViolation {
        private String invalidValue;
        private String propertyPath;
        private String message;

        private ConstraintViolation(String invalidValue, String propertyPath, String message) {
            this.invalidValue = invalidValue;
            this.propertyPath = propertyPath;
            this.message = message;
        }

        public static List<ConstraintViolation> of(Set<javax.validation.ConstraintViolation<?>> constraintViolation){
            return constraintViolation.stream().map(
                    violation -> new ConstraintViolation(violation.getInvalidValue().toString(),
                            violation.getPropertyPath().toString(), violation.getMessage())
            ).collect(Collectors.toList());
        }
    }
}
