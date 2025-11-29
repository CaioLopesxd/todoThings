package api.main.controllers;

import api.main.dtos.task.CreateTaskDto;
import api.main.dtos.task.TaskStepDto;
import api.main.dtos.task.UpdateTaskDto;
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
    public ResponseEntity<Task> post(@RequestBody @Valid CreateTaskDto createTaskDto) {
        User currentUser = getCurrentUser();
        Task task = taskService.createTask(createTaskDto, currentUser);
        return  ResponseEntity.ok(task);
    }

    @PostMapping("/{taskId}/taskstep")
    public ResponseEntity<Task> post(@PathVariable("taskId") int taskId,
                                     @RequestBody TaskStepDto taskStepDto) {
        User currentUser = getCurrentUser();

        Task task = taskService.createTaskStep(taskId, taskStepDto, currentUser);

        if (task == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(task);
    }

    @GetMapping()
    public ResponseEntity<List<Task>> getAllTasks() {
        User currentUser = getCurrentUser();
        List<Task> tasks = taskService.getAllTasksByUser(currentUser);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<Task> getById(@PathVariable("taskId") int taskId) {
        User currentUser = getCurrentUser();

        Task task = taskService.getTaskById(taskId, currentUser);
        return ResponseEntity.ok(task);
    }

    @GetMapping(value = "/export", produces = "text/csv")
    public ResponseEntity<Resource> exportTasksCsv() throws IOException {
        User currentUser = getCurrentUser();
        List<Task> tasks = taskService.getAllTasksByUser(currentUser);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (CSVPrinter csvPrinter = new CSVPrinter(
                new OutputStreamWriter(out, StandardCharsets.ISO_8859_1),
                CSVFormat.EXCEL.builder().setHeader("Id", "Titulo", "Descrição", "Passos").build()
        )) {

            for (Task t : tasks) {
                List<TaskStep> taskSteps = t.getTaskSteps();

                StringBuilder taskStepConcat = new StringBuilder();
                for (TaskStep ts : taskSteps) {
                    taskStepConcat.append(ts.getDescription()).append(" ");
                }

                csvPrinter.printRecord(
                        t.getId(),
                        t.getTitle(),
                        t.getDescription(),
                        taskStepConcat.toString()
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

    @PutMapping("/{taskId}")
    public ResponseEntity<Task> putTask(@PathVariable("taskId")int taskId,@RequestBody UpdateTaskDto updateTaskDto) {
        User currentUser = getCurrentUser();
        Task task = taskService.updateTask(taskId, updateTaskDto ,currentUser);
        return ResponseEntity.ok(task);
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<String> putTask(@PathVariable("taskId")int taskId) {
        User currentUser = getCurrentUser();
        taskService.deleteTask(taskId, currentUser);
        return ResponseEntity.ok("Deletado com sucesso");
    }


}