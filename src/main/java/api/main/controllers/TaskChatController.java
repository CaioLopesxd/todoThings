package api.main.controllers;

import api.main.dtos.task.ChatMessageDto;
import api.main.dtos.task.NewChatMessageRequest;
import api.main.exceptions.auth.UserNotAuthenticated;
import api.main.exceptions.auth.UserNotFound;
import api.main.exceptions.task.TaskNotFound;
import api.main.exceptions.task.UserNotAssignedToTask;
import api.main.models.ChatMessage;
import api.main.models.Task;
import api.main.models.User;
import api.main.repositories.ChatMessageRepository;
import api.main.repositories.TaskRepository;
import api.main.repositories.UserRepository;
import api.main.service.TaskService;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Map;
import java.util.UUID; // <--- Importante

@Controller
public class TaskChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageRepository chatMessageRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskService taskService;

    public TaskChatController(SimpMessagingTemplate messagingTemplate,
                              ChatMessageRepository chatMessageRepository,
                              TaskRepository taskRepository,
                              UserRepository userRepository, TaskService taskService) {
        this.messagingTemplate = messagingTemplate;
        this.chatMessageRepository = chatMessageRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.taskService = taskService;
    }

    @MessageMapping("/task/{taskId}/chat")
    public void receiveMessage(@DestinationVariable Integer taskId,
                               @Payload NewChatMessageRequest msg,
                               @Header("simpSessionAttributes") Map<String, Object> sessionAttrs) {


        Object userIdObj = sessionAttrs != null ? sessionAttrs.get("userId") : null;
        if (userIdObj == null) {
            throw new UserNotAuthenticated();
        }

        UUID userId;
        try {
            userId = UUID.fromString(userIdObj.toString());
        } catch (IllegalArgumentException e) {
            throw new UserNotAuthenticated("UUID com problemas");
        }

        User sender = userRepository.findById(userId)
                .orElseThrow(UserNotFound::new);

        ChatMessageDto dto = taskService.sendMessageToTask(taskId, msg.content(), sender);
        messagingTemplate.convertAndSend("/topic/task/" + taskId + "/chat", dto);
    }
}