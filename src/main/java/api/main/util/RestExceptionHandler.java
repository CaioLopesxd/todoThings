package api.main.util;
import api.main.exceptions.auth.ContactAlreadyExists;
import api.main.exceptions.auth.EmailOrPasswordError;
import api.main.exceptions.auth.UserNotAuthenticated;
import api.main.exceptions.auth.UserNotFound;
import api.main.exceptions.task.TaskStepNotFound;
import api.main.exceptions.task.UserAlreadyAssignToTask;
import api.main.exceptions.task.UserNotAssignedToTask;
import api.main.exceptions.task.UserNotOwnerOfTask;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserNotOwnerOfTask.class)
    private ResponseEntity<Object> userNotOwnerOfTaskHandler(UserNotOwnerOfTask exception) {
        return buildErrorResponse(exception.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(EmailOrPasswordError.class)
    private ResponseEntity<Object> emailOrPasswordErrorHandler(EmailOrPasswordError exception){
        return buildErrorResponse(exception.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserNotAuthenticated.class)
    private ResponseEntity<Object> userNotAuthenticatedHandler(UserNotAuthenticated exception){
        return buildErrorResponse(exception.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ContactAlreadyExists.class)
    private ResponseEntity<Object> contactAlreadyExistsHandler(ContactAlreadyExists exception){
        return buildErrorResponse(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFound.class)
    private ResponseEntity<Object> userNotFoundHandler(UserNotFound exception){
        return buildErrorResponse(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotAssignedToTask.class)
    private ResponseEntity<Object> userNotAssignedToTaskHandler(UserNotAssignedToTask exception){
        return buildErrorResponse(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserAlreadyAssignToTask.class)
    private ResponseEntity<Object> userAlreadyAssignToTaskHandler(UserAlreadyAssignToTask exception){
        return buildErrorResponse(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(TaskStepNotFound.class)
    private ResponseEntity<Object> taskStepNotFoundHandler(TaskStepNotFound exception){
        return buildErrorResponse(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        Map<String, String> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage,
                        (msg1, msg2) -> msg1
                ));

        Map<String, Object> body = new LinkedHashMap<>();
        String message = fieldErrors.values().stream().findFirst().orElse("Erro");
        body.put("message", message);
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", HttpStatus.BAD_REQUEST.value());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(body);
    }


    private ResponseEntity<Object> buildErrorResponse(String message, HttpStatus status) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("message", message);
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", status.value());

        return ResponseEntity.status(status).body(body);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            org.springframework.http.converter.HttpMessageNotReadableException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        String message = "Requisição mal formatada";

        if (ex.getCause() instanceof InvalidFormatException invalidFormatException) {
            Class<?> targetType = invalidFormatException.getTargetType();
            if (targetType != null && targetType.isEnum()) {
                String invalidValue = invalidFormatException.getValue().toString();
                Object[] acceptedValues = targetType.getEnumConstants();
                message = String.format(
                        "Valor '%s' inválido. Valores aceitos: %s",
                        invalidValue,
                        java.util.Arrays.toString(
                                java.util.Arrays.stream(acceptedValues)
                                        .map(Object::toString)
                                        .toArray()
                        )
                );
            }
        }

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("message", message);
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", HttpStatus.BAD_REQUEST.value());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }
}

