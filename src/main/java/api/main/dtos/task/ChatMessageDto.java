package api.main.dtos.task;

import java.time.LocalDateTime;

public record ChatMessageDto(
        String content,
        String senderName,
        LocalDateTime sentAt
) {}
