package api.main.exceptions.auth;

public class ContactAlreadyExists extends RuntimeException {
    public ContactAlreadyExists(String message) {
        super(message);
    }
    public ContactAlreadyExists(){ super("Contato ja cadastrado.");}
}
