package api.main.mappers.task;

import api.main.dtos.task.ChatMessageDto;
import api.main.dtos.task.TaskResponseDto;
import api.main.models.ChatMessage;
import api.main.models.Task;
import api.main.models.User;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TaskMapper {

    public TaskResponseDto toTaskResponseDto(Task task) {

        List<ChatMessageDto> chatMessageDto = task.getChatMessages() != null
                ? task.getChatMessages().stream()
                .map(this::toChatMessageDto)
                .collect(Collectors.toList())
                : Collections.emptyList();

        return new TaskResponseDto(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getTaskStatus(),
                task.getTaskOwner().getName(),
                chatMessageDto,
                task.getTaskSteps(),
                task.getAssignedUsers().stream().map(User::getName).collect(Collectors.toList())
        );
    }

    public ChatMessageDto toChatMessageDto(ChatMessage message) {
        if (message == null) {
            return null;
        }

        return new ChatMessageDto(
                message.getContent(),
                message.getSender().getName(),
                message.getSentAt()
        );
    }
}