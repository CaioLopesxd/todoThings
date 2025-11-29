package api.main.exceptions.task;

public class UserNotOwnerOfTask extends RuntimeException {
    public UserNotOwnerOfTask() { super("Usuario não é dono da tarefa.");}
    public UserNotOwnerOfTask(String message) {
        super(message);
    }
}
