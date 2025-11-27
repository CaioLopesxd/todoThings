package api.main.util;
import api.main.exceptions.auth.EmailOrPasswordError;
import api.main.exceptions.auth.UserNotAuthenticated;
import api.main.exceptions.task.UserNotOwnerOfTask;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(UserNotOwnerOfTask.class)
    private ResponseEntity<String> userNotOwnerOfTaskHandler(UserNotOwnerOfTask exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario não tem acesso a essa tarefa.");
    }

    @ExceptionHandler(EmailOrPasswordError.class)
    private ResponseEntity<String> emailOrPasswordErrorHandler(EmailOrPasswordError exception){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email ou Senha incorretos.");
    }
    @ExceptionHandler(UserNotAuthenticated.class)
    private ResponseEntity<String> userNotAuthenticatedHandler(UserNotAuthenticated exception){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario Não autenticado.");
    }
}
