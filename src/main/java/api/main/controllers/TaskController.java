package api.main.controllers;

import api.main.dtos.task.TaskDto;
import api.main.dtos.task.TaskStepDto;
import api.main.models.Task;
import api.main.models.TaskStep;
import api.main.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/task")
class TaskController {
    TaskService taskService;

    public TaskController(TaskService _taskService) {
        this.taskService = _taskService;
    }
    @PostMapping()
    public ResponseEntity<Task> post(@RequestBody TaskDto taskDto) {
        Task task = taskService.createTask(taskDto);
        return  ResponseEntity.ok(task);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<Task> getById(@PathVariable("taskId") int taskId) {
        Task task = taskService.getTaskById(taskId);
        return ResponseEntity.ok(task);
    }

    @PostMapping("/{taskId}/taskstep")
    public ResponseEntity<Task> post(@PathVariable("taskId") int taskId,
                                     @RequestBody TaskStepDto taskStepDto) {
        Task task = taskService.createTaskStep(taskId,taskStepDto);
        return ResponseEntity.ok(task);
    }
}
