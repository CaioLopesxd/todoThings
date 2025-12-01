package api.main.controllers;

import api.main.dtos.auth.NewContactDto;
import api.main.dtos.task.*;
import api.main.models.Task;
import api.main.models.TaskStep;
import api.main.models.User;
import api.main.service.TaskService;
import api.main.util.SecurityUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

import jakarta.validation.Valid;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/task")
public class TaskController {
    TaskService taskService;

    public TaskController(TaskService _taskService) {
        this.taskService = _taskService;
    }

    private User getCurrentUser() {
        return SecurityUtils.getCurrentUser();
    }

    @PostMapping()
    public ResponseEntity<TaskResponseDto> post(@RequestBody @Valid CreateTaskDto createTaskDto) {
        User currentUser = getCurrentUser();
        TaskResponseDto task = taskService.createTask(createTaskDto, currentUser);
        return  ResponseEntity.ok(task);
    }

    @GetMapping()
    public ResponseEntity<List<TaskResponseDto>> getAllTasks() {
        User currentUser = getCurrentUser();
        List<TaskResponseDto> tasks = taskService.getAllTasksByUser(currentUser);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskResponseDto> getById(@PathVariable("taskId") int taskId) {
        User currentUser = getCurrentUser();
        TaskResponseDto task = taskService.getTaskById(taskId, currentUser);
        return ResponseEntity.ok(task);
    }

    @GetMapping(value = "/export", produces = "text/csv")
    public ResponseEntity<Resource> exportTasksCsv() throws IOException {
        User currentUser = getCurrentUser();
        List<TaskResponseDto> tasks = taskService.getAllTasksByUser(currentUser);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (CSVPrinter csvPrinter = new CSVPrinter(
                new OutputStreamWriter(out, StandardCharsets.UTF_8),
                CSVFormat.EXCEL.builder().setHeader("Id", "Titulo", "Descrição", "Passos","Usuarios Anexados","Mensagens").build()
        )) {

            for (TaskResponseDto t : tasks) {
                List<TaskStep> taskSteps = t.taskSteps();

                StringBuilder taskStepConcat = new StringBuilder();
                for (TaskStep ts : taskSteps) {
                    taskStepConcat.append(ts.getDescription()).append("; ");
                }

                StringBuilder assignedUsersConcat = new StringBuilder();

                for (String user : t.assignedUsers()) {
                    assignedUsersConcat.append(user).append("; ");
                }

                StringBuilder chatMessagesConcat = new StringBuilder();

                for (ChatMessageDto chatMessage : t.chatMessages()) {
                    chatMessagesConcat.append(chatMessage.senderName()).append(": ").append(chatMessage.content()).append("; ");
                }

                csvPrinter.printRecord(
                        t.id(),
                        t.title(),
                        t.description(),
                        taskStepConcat.toString(),
                        assignedUsersConcat,
                        chatMessagesConcat
                );
            }

            csvPrinter.flush();
        }

        ByteArrayResource resource = new ByteArrayResource(out.toByteArray());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=tasks.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .contentLength(resource.contentLength())
                .body(resource);
    }

    @PatchMapping("/{taskId}")
    public ResponseEntity<TaskResponseDto> putTask(@PathVariable("taskId")int taskId,@RequestBody  UpdateTaskDto updateTaskDto) {
        User currentUser = getCurrentUser();
        TaskResponseDto task = taskService.updateTask(taskId, updateTaskDto ,currentUser);
        return ResponseEntity.ok(task);
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<String> putTask(@PathVariable("taskId") int taskId) {
        User currentUser = getCurrentUser();
        taskService.deleteTask(taskId, currentUser);
        return ResponseEntity.ok("Deletado com sucesso");
    }

    @PostMapping("/{taskId}/assignuser")
    public ResponseEntity<TaskResponseDto> postAssignUserToTask(@PathVariable("taskId") int taskId,@RequestBody @Valid NewContactDto newContactDto) {
        User currentUser = getCurrentUser();
        TaskResponseDto task = taskService.assignContactToTask(taskId, newContactDto.email(), currentUser);
        return ResponseEntity.ok(task);
    }

    @DeleteMapping("/{taskId}/assignuser")
    public ResponseEntity<TaskResponseDto> deleteAssignedUserFromTask(
            @PathVariable("taskId") int taskId,
            @RequestBody @Valid NewContactDto newContactDto) {

        User currentUser = getCurrentUser();
        TaskResponseDto task = taskService.removeContactFromTask(taskId, newContactDto.email(), currentUser);
        return ResponseEntity.ok(task);
    }

    @PostMapping("/{taskId}/taskstep")
    public ResponseEntity<TaskResponseDto> post(@PathVariable("taskId") int taskId,
                                     @RequestBody @Valid TaskStepDto taskStepDto) {
        User currentUser = getCurrentUser();
        TaskResponseDto task = taskService.createTaskStep(taskId, taskStepDto, currentUser);
        return ResponseEntity.ok(task);
    }

    @PatchMapping("/{taskId}/taskstep/{taskStepId}")
    public ResponseEntity<TaskResponseDto> patch(@PathVariable("taskId") int taskId,
                                      @PathVariable("taskStepId") int taskStepId,
                                      @RequestBody UpdateTaskStepDto updateTaskStepDto) {
        User currentUser = getCurrentUser();
        TaskResponseDto task = taskService.updateTaskStep(taskId, taskStepId, updateTaskStepDto, currentUser);
        return ResponseEntity.ok(task);
    }

    @DeleteMapping("/{taskId}/taskstep/{taskStepId}")
    public ResponseEntity<Task> deleteTaskStep(@PathVariable("taskId") int taskId, @PathVariable("taskStepId") int taskStepId) {
        User currentUser = getCurrentUser();
        taskService.deleteTaskStep(taskId, taskStepId, currentUser);
        return ResponseEntity.ok().build();
    }
}