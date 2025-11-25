package api.main.service;

import api.main.dtos.task.TaskDto;
import api.main.dtos.task.TaskStepDto;
import api.main.models.Task;
import api.main.models.TaskStatus;
import api.main.models.TaskStep;
import api.main.models.User;
import api.main.repositories.TaskRepository;
import api.main.repositories.TaskStepRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskStepRepository taskStepRepository;

    public TaskService(TaskRepository _taskRepository,
                       TaskStepRepository _taskStepRepository) {
        this.taskRepository = _taskRepository;
        this.taskStepRepository = _taskStepRepository;
    }
    
    public Task createTask(TaskDto taskDto, User user) {
        Task task = new Task(taskDto.title(),
                             taskDto.description(),
                             TaskStatus.PENDENTE);
        task.setUser(user);

        return taskRepository.save(task);
    }
    
    public Task createTaskStep(int taskId, TaskStepDto taskStepDto, User user) {
        Optional<Task> taskOptional = taskRepository.findById(taskId);
        if (taskOptional.isEmpty()) {
            return null;
        }
        
        Task task = taskOptional.get();
        if (!task.getUser().getId().equals(user.getId())) {
            return null;
        }
        
        TaskStep taskStep = new TaskStep(task, taskStepDto.description(), TaskStatus.PENDENTE);
        taskStepRepository.save(taskStep);

        return task;
    }

    public Task getTaskById(int taskId, User user) {
        return taskRepository.findByIdAndUser(taskId, user)
                .orElseThrow(() -> new RuntimeException("Task não encontrada ou não pertence ao usuário"));
    }
    
    public List<Task> getAllTasksByUser(User user) {
        return taskRepository.findByUser(user);
    }

    public Task updateTask(int TaskId, TaskDto taskDto) {
        Task task = taskRepository.findById(TaskId).get();

        return taskRepository.save(task);
    }
    
    public Task deleteTask(int id) {
        taskRepository.deleteById(id);

        return taskRepository.findById(id).get();
    }

}
