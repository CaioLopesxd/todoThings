package api.main.dtos.task;

import jakarta.validation.constraints.NotBlank;

public record TaskStepDto(@NotBlank(message = "Preencha a descrição.") String description) {
}
