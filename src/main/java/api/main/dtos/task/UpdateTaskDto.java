package api.main.dtos.task;

import api.main.models.TaskStatus;
public record UpdateTaskDto( String title, String description,TaskStatus taskStatus) {
}
