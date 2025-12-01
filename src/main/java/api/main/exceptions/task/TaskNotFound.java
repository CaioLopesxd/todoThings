package api.main.exceptions.task;

public class TaskNotFound extends RuntimeException {
    public TaskNotFound(String message) {
        super(message);
    }
    public TaskNotFound(){ super("Tarefa não encontrada.");}
}
