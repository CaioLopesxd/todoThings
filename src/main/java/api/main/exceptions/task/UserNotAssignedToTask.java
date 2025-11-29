package api.main.exceptions.task;

public class UserNotAssignedToTask extends RuntimeException {
    public UserNotAssignedToTask(String message) {
        super(message);
    }
    public UserNotAssignedToTask(){ super("Usuario não atribuido a tarefa.");}
}
