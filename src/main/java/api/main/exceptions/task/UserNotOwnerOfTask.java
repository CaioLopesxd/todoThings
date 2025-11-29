package api.main.exceptions.task;

public class UserNotOwnerOfTask extends RuntimeException {
    public UserNotOwnerOfTask() { super("Essa tarefa não pertence a esse usuario");}
    public UserNotOwnerOfTask(String message) {
        super(message);
    }
}
