package api.main.service;

import api.main.dtos.task.CreateTaskDto;
import api.main.dtos.task.TaskStepDto;
import api.main.dtos.task.UpdateTaskDto;
import api.main.dtos.task.UpdateTaskStepDto;
import api.main.exceptions.auth.UserNotFound;
import api.main.exceptions.task.TaskStepNotFound;
import api.main.exceptions.task.UserAlreadyAssignToTask;
import api.main.exceptions.task.UserNotOwnerOfTask;
import api.main.exceptions.task.UserNotAssignedToTask;
import api.main.models.Task;
import api.main.models.TaskStatus;
import api.main.models.TaskStep;
import api.main.models.User;
import api.main.repositories.TaskRepository;
import api.main.repositories.TaskStepRepository;
import api.main.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskStepRepository taskStepRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository _taskRepository,
                       TaskStepRepository _taskStepRepository,
                       UserRepository userRepository) {
        this.taskRepository = _taskRepository;
        this.taskStepRepository = _taskStepRepository;
        this.userRepository = userRepository;
    }
    
    public Task createTask(CreateTaskDto createTaskDto, User taskOwner) {
        Task task = new Task(createTaskDto.title(),
                             createTaskDto.description(),
                             TaskStatus.PENDENTE,
                             taskOwner);
        task.getAssignedUsers().add(taskOwner);

        return taskRepository.save(task);
    }

    public Task getTaskById(int taskId, User user) {
        return taskRepository.findByIdAndAssignedUsersContains(taskId, user)
                .orElseThrow(UserNotAssignedToTask::new);
    }
    
    public List<Task> getAllTasksByUser(User user) {
        return taskRepository.findByAssignedUsersContains(user);
    }

    public Task updateTask(int id, UpdateTaskDto updateTaskDto, User owner) {
        Task task = taskRepository.findByIdAndTaskOwner(id, owner)
                .orElseThrow(UserNotOwnerOfTask::new);

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
                .orElseThrow(UserNotOwnerOfTask::new);

        taskRepository.delete(task);
    }

    public Task assignContactToTask(int id, String email, User owner) {
        Task task = taskRepository.findByIdAndTaskOwner(id, owner)
                .orElseThrow(UserNotOwnerOfTask::new);

        User contact = userRepository.findByEmail(email)
                .orElseThrow(UserNotFound::new);

        if (contact.equals(task.getTaskOwner())) {
            throw new UserAlreadyAssignToTask();
        }

        if (task.getAssignedUsers().contains(contact)) {
            throw new UserAlreadyAssignToTask();
        }

        task.getAssignedUsers().add(contact);

        return taskRepository.save(task);
    }

    public Task removeContactFromTask(int id, String email, User owner) {
        Task task = taskRepository.findByIdAndTaskOwner(id, owner)
                .orElseThrow(UserNotOwnerOfTask::new);

        User contact = userRepository.findByEmail(email)
                .orElseThrow(UserNotFound::new);

        if (!task.getAssignedUsers().contains(contact)) {
            throw new UserNotAssignedToTask();
        }

        task.getAssignedUsers().remove(contact);

        return taskRepository.save(task);
    }
    public Task createTaskStep(int taskId, TaskStepDto taskStepDto, User user) {
        Task task = taskRepository.findByIdAndTaskOwner(taskId,user).orElseThrow(UserNotOwnerOfTask::new);

        TaskStep taskStep = new TaskStep(task, taskStepDto.description(), TaskStatus.PENDENTE);
        taskStepRepository.save(taskStep);

        return task;
    }

    public Task updateTaskStep(int taskId, int stepId, UpdateTaskStepDto dto, User user) {

        Task task = taskRepository.findByIdAndAssignedUsersContains(taskId, user)
                .orElseThrow(UserNotAssignedToTask::new);

        TaskStep taskStep = taskStepRepository.findByIdAndTaskId(stepId, taskId)
                .orElseThrow(TaskStepNotFound::new);

        if (dto.description() != null) {
            taskStep.setDescription(dto.description());
        }

        if (dto.taskStatus() != null) {
            taskStep.setStepStatus(dto.taskStatus());
        }

        return taskRepository.save(task);
    }


    public void deleteTaskStep(int taskId, int stepId, User user) {

        Task task = taskRepository.findByIdAndTaskOwner(taskId, user)
                .orElseThrow(UserNotOwnerOfTask::new);

        TaskStep step = taskStepRepository.findByIdAndTaskId(stepId,taskId)
                .orElseThrow();

        task.getTaskSteps().remove(step);

        taskRepository.save(task);
    }


}
