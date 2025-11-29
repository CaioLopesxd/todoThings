package api.main.service;

import api.main.dtos.task.CreateTaskDto;
import api.main.dtos.task.TaskStepDto;
import api.main.dtos.task.UpdateTaskDto;
import api.main.exceptions.task.UserNotOwnerOfTask;
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
    
    public Task createTask(CreateTaskDto createTaskDto, User taskOwner) {
        Task task = new Task(createTaskDto.title(),
                             createTaskDto.description(),
                             TaskStatus.PENDENTE,
                             taskOwner);

        return taskRepository.save(task);
    }
    
    public Task createTaskStep(int taskId, TaskStepDto taskStepDto, User user) {
        Optional<Task> taskOptional = taskRepository.findById(taskId);
        if (taskOptional.isEmpty()) {
            return null;
        }
        
        Task task = taskOptional.get();
        if (!task.getTaskOwner().getId().equals(user.getId())) {
            return null;
        }
        
        TaskStep taskStep = new TaskStep(task, taskStepDto.description(), TaskStatus.PENDENTE);
        taskStepRepository.save(taskStep);

        return task;
    }

    public Task getTaskById(int taskId, User user) {
        return taskRepository.findByIdAndTaskOwner(taskId, user)
                .orElseThrow(UserNotOwnerOfTask::new);
    }
    
    public List<Task> getAllTasksByUser(User user) {
        return taskRepository.findByTaskOwner(user);
    }

    public Task updateTask(int id, UpdateTaskDto updateTaskDto, User user) {
        Task task = taskRepository.findByIdAndTaskOwner(id, user)
                .orElseThrow(UserNotOwnerOfTask::new); ;

        if (updateTaskDto.title() != null) {
            task.setTitle(updateTaskDto.title());
        }

        if (updateTaskDto.description() != null) {
            task.setDescription(updateTaskDto.description());
        }

        if (updateTaskDto.taskStatus() != null) {
            task.setTaskStatus(updateTaskDto.taskStatus());
        }

        return taskRepository.save(task);
    }

    public void deleteTask(int id, User user) {
        Task task = taskRepository.findByIdAndTaskOwner(id, user)
                .orElseThrow(UserNotOwnerOfTask::new); ;

        taskRepository.delete(task);
    }

}
