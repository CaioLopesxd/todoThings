package api.main.dtos.task;

import jakarta.validation.constraints.NotBlank;

public record CreateTaskDto(@NotBlank(message = "Titulo Não Pode Ser Vazio.") String title,
                            @NotBlank(message = "Descrição Não Pode Ser Vazia") String description) {}
