package api.main.exceptions.auth;

public class AuthException extends RuntimeException {
    public AuthException(String message) {
        super(message);
    }
}
