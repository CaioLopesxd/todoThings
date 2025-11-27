package api.main.exceptions.task;

public class UserNotOwnerOfTask extends RuntimeException {
    public UserNotOwnerOfTask() { super("Usuario não autenticado");}
    public UserNotOwnerOfTask(String message) {
        super(message);
    }
}
