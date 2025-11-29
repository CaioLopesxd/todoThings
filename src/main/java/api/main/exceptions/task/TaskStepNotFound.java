package api.main.exceptions.task;

public class TaskStepNotFound extends RuntimeException {
    public TaskStepNotFound(String message) {
        super(message);
    }
    public TaskStepNotFound(){ super("Passo da tarefa não encontrado.");}
}
