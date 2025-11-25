package api.main.exceptions.auth;

public class AuthException extends RuntimeException {
    public AuthException() {
        super("Task não encontrada ou não pertence ao usuário");
    }
}
