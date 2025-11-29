package api.main.exceptions.task;

public class UserAlreadyAssignToTask extends RuntimeException {
    public UserAlreadyAssignToTask(String message) {
        super(message);
    }
    public UserAlreadyAssignToTask(){ super("Usuario ja atribuido a tarefa.");}
}
