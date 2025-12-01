package api.main.dtos.task;

import jakarta.validation.constraints.NotBlank;

public record NewChatMessageRequest (@NotBlank(message = "Mensagem não pode estar vazia") String content){}
