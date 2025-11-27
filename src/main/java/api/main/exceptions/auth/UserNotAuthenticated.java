package api.main.exceptions.auth;

public class UserNotAuthenticated extends RuntimeException {
    public UserNotAuthenticated(String message) { super(message); }
    public UserNotAuthenticated() { super("Usuario não Autenticado"); }
}
