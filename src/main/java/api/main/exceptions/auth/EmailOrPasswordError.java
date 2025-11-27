package api.main.exceptions.auth;

public class EmailOrPasswordError extends RuntimeException {
    public EmailOrPasswordError(String message) { super(message); }
    public EmailOrPasswordError() { super("Senha ou Email Incorretos");}

}
