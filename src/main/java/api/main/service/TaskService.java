package api.main.service;

import api.main.dtos.task.TaskDto;
import api.main.dtos.task.TaskStepDto;
import api.main.models.Task;
import api.main.models.TaskStatus;
import api.main.models.TaskStep;
import api.main.models.User;
import api.main.repositories.TaskRepository;
import api.main.repositories.TaskStatusRepository;
import api.main.repositories.TaskStepRepository;
import api.main.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TaskService {
    private TaskRepository taskRepository;
    private UserRepository userRepository;
    private TaskStatusRepository taskStatusRepository;
    private TaskStepRepository taskStepRepository;

    public TaskService(TaskRepository _taskRepository,
                       UserRepository _userRepository,
                       TaskStatusRepository _taskStatusRepository,
                       TaskStepRepository _taskStepRepository) {
        this.taskRepository = _taskRepository;
        this.userRepository = _userRepository;
        this.taskStatusRepository = _taskStatusRepository;
        this.taskStepRepository = _taskStepRepository;
    }

    public Task createTask(TaskDto taskDto) {
        TaskStatus taskStatus = taskStatusRepository.findById(1).get();
        Task task = new Task(taskDto.title(),
                             taskDto.description(),
                             taskStatus);
        User user = userRepository.findById(UUID.fromString(taskDto.userId())).get();
        task.setUser(user);

        return taskRepository.save(task);
    }
    
    public Task createTask(TaskDto taskDto, User user) {
        TaskStatus taskStatus = taskStatusRepository.findById(1).get();
        Task task = new Task(taskDto.title(),
                             taskDto.description(),
                             taskStatus);
        task.setUser(user);

        return taskRepository.save(task);
    }

    public Task createTaskStep(int taskId, TaskStepDto taskStepDto) {
        Task task = taskRepository.findById(taskId).get();
        TaskStatus taskStatus = taskStatusRepository.findById(1).get();
        TaskStep taskStep = new TaskStep(task, taskStepDto.description(), taskStatus);
        taskStepRepository.save(taskStep);

        return task;
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
        
        TaskStatus taskStatus = taskStatusRepository.findById(1).get();
        TaskStep taskStep = new TaskStep(task, taskStepDto.description(), taskStatus);
        taskStepRepository.save(taskStep);

        return task;
    }

    public Task getTaskById(int taskId) {
        return taskRepository.findById(taskId).get();
    }
    
    public Task getTaskById(int taskId, User user) {
        Optional<Task> taskOptional = taskRepository.findById(taskId);
        if (taskOptional.isEmpty()) {
            return null;
        }
        
        Task task = taskOptional.get();
        if (!task.getUser().getId().equals(user.getId())) {
            return null;
        }
        
        return task;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
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
