package api.main.dtos.task;

import api.main.models.TaskStatus;

public record UpdateTaskStepDto(String description, TaskStatus taskStatus) {
}
