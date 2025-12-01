package api.main.service;

import api.main.dtos.task.*;
import api.main.exceptions.auth.UserNotFound;
import api.main.exceptions.task.TaskStepNotFound;
import api.main.exceptions.task.UserAlreadyAssignToTask;
import api.main.exceptions.task.UserNotOwnerOfTask;
import api.main.exceptions.task.UserNotAssignedToTask;
import api.main.mappers.task.TaskMapper;
import api.main.models.*;
import api.main.repositories.ChatMessageRepository;
import api.main.repositories.TaskRepository;
import api.main.repositories.TaskStepRepository;
import api.main.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskStepRepository taskStepRepository;
    private final UserRepository userRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final TaskMapper taskMapper; // Agora é injetado, não instanciado com 'new'

    public TaskService(TaskRepository _taskRepository,
                       TaskStepRepository _taskStepRepository,
                       UserRepository _userRepository,
                       ChatMessageRepository _chatMessageRepository,
                       TaskMapper _taskMapper) {
        this.taskRepository = _taskRepository;
        this.taskStepRepository = _taskStepRepository;
        this.userRepository = _userRepository;
        this.chatMessageRepository = _chatMessageRepository;
        this.taskMapper = _taskMapper;
    }

    private TaskResponseDto convertAndReturnDto(Task task) {
        List<ChatMessage> messages = chatMessageRepository.findByTaskIdOrderBySentAtAsc(task.getId());
        task.setChatMessages(messages);
        return taskMapper.toTaskResponseDto(task);
    }

    public TaskResponseDto createTask(CreateTaskDto createTaskDto, User taskOwner) {
        Task task = new Task(createTaskDto.title(),
                createTaskDto.description(),
                TaskStatus.PENDENTE,
                taskOwner);
        task.getAssignedUsers().add(taskOwner);

        Task savedTask = taskRepository.save(task);
        return convertAndReturnDto(savedTask);
    }

    public TaskResponseDto getTaskById(int taskId, User user) {
        Task task = taskRepository.findByIdAndAssignedUsersContains(taskId, user)
                .orElseThrow(UserNotAssignedToTask::new);

        return convertAndReturnDto(task);
    }

    public List<TaskResponseDto> getAllTasksByUser(User user) {
        List<Task> tasks = taskRepository.findByAssignedUsersContains(user);

        return tasks.stream()
                .map(this::convertAndReturnDto)
                .toList();
    }

    public TaskResponseDto updateTask(int id, UpdateTaskDto updateTaskDto, User owner) {
        Task task = taskRepository.findByIdAndTaskOwner(id, owner)
                .orElseThrow(UserNotOwnerOfTask::new);

        if (updateTaskDto.title() != null) task.setTitle(updateTaskDto.title());
        if (updateTaskDto.description() != null) task.setDescription(updateTaskDto.description());
        if (updateTaskDto.taskStatus() != null) task.setTaskStatus(updateTaskDto.taskStatus());

        Task savedTask = taskRepository.save(task);
        return convertAndReturnDto(savedTask);
    }

    public void deleteTask(int id, User user) {
        Task task = taskRepository.findByIdAndTaskOwner(id, user)
                .orElseThrow(UserNotOwnerOfTask::new);
        taskRepository.delete(task);
    }

    public TaskResponseDto assignContactToTask(int id, String email, User owner) {
        Task task = taskRepository.findByIdAndTaskOwner(id, owner)
                .orElseThrow(UserNotOwnerOfTask::new);

        User contact = userRepository.findByEmail(email)
                .orElseThrow(UserNotFound::new);

        if (contact.equals(task.getTaskOwner()) || task.getAssignedUsers().contains(contact)) {
            throw new UserAlreadyAssignToTask();
        }

        task.getAssignedUsers().add(contact);

        Task savedTask = taskRepository.save(task);
        return convertAndReturnDto(savedTask);
    }

    public TaskResponseDto removeContactFromTask(int id, String email, User owner) {
        Task task = taskRepository.findByIdAndTaskOwner(id, owner)
                .orElseThrow(UserNotOwnerOfTask::new);

        User contact = userRepository.findByEmail(email)
                .orElseThrow(UserNotFound::new);

        if (!task.getAssignedUsers().contains(contact)) {
            throw new UserNotAssignedToTask();
        }

        task.getAssignedUsers().remove(contact);

        Task savedTask = taskRepository.save(task);
        return convertAndReturnDto(savedTask);
    }

    public TaskResponseDto createTaskStep(int taskId, TaskStepDto taskStepDto, User user) {
        Task task = taskRepository.findByIdAndTaskOwner(taskId, user)
                .orElseThrow(UserNotOwnerOfTask::new);

        TaskStep taskStep = new TaskStep(task, taskStepDto.description(), TaskStatus.PENDENTE);
        taskStepRepository.save(taskStep);

        return convertAndReturnDto(task);
    }

    public TaskResponseDto updateTaskStep(int taskId, int stepId, UpdateTaskStepDto dto, User user) {
        Task task = taskRepository.findByIdAndAssignedUsersContains(taskId, user)
                .orElseThrow(UserNotAssignedToTask::new);

        TaskStep taskStep = taskStepRepository.findByIdAndTaskId(stepId, taskId)
                .orElseThrow(TaskStepNotFound::new);

        if (dto.description() != null) taskStep.setDescription(dto.description());
        if (dto.taskStatus() != null) taskStep.setStepStatus(dto.taskStatus());

        taskStepRepository.save(taskStep);

        return convertAndReturnDto(task);
    }


    public TaskResponseDto deleteTaskStep(int taskId, int stepId, User user) {
        Task task = taskRepository.findByIdAndTaskOwner(taskId, user)
                .orElseThrow(UserNotOwnerOfTask::new);

        TaskStep step = taskStepRepository.findByIdAndTaskId(stepId, taskId)
                .orElseThrow(TaskStepNotFound::new);

        task.getTaskSteps().remove(step);
        taskStepRepository.delete(step);
        return convertAndReturnDto(task);
    }

    public ChatMessageDto sendMessageToTask(int taskId, String content, User user) {
        Task task = taskRepository.findByIdAndAssignedUsersContains(taskId, user)
                .orElseThrow(UserNotAssignedToTask::new);

        ChatMessage chatMessage = chatMessageRepository.save(new ChatMessage(task, user, content));

        return new ChatMessageDto(chatMessage.getContent(), user.getName(), chatMessage.getSentAt());
    }
}