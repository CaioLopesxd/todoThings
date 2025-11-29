package api.main.exceptions.auth;

public class UserNotFound extends RuntimeException {
    public UserNotFound(String message) {
        super(message);
    }
    public UserNotFound(){ super("Usuario não encontrado.");}
}
