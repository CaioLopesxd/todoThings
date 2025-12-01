package api.main.dtos.task;

import api.main.models.TaskStatus;
import api.main.models.TaskStep;

import java.util.List;

public record TaskResponseDto(
        int id,
        String title,
        String description,
        TaskStatus status,
        String ownerName,
        List<ChatMessageDto> chatMessages,
        List<TaskStep> taskSteps,
        List<String> assignedUsers
) {}
