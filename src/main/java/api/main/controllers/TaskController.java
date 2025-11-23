package api.main.controllers;

import api.main.dtos.task.TaskDto;
import api.main.dtos.task.TaskStepDto;
import api.main.models.Task;
import api.main.models.TaskStep;
import api.main.service.TaskService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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

    @GetMapping(value = "/export", produces = "text/csv")
    public ResponseEntity<Resource> exportTasksCsv() throws IOException {

        List<Task> tasks = taskService.getAllTasks();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVPrinter csvPrinter = new CSVPrinter(
                new OutputStreamWriter(out, StandardCharsets.ISO_8859_1),
                CSVFormat.EXCEL.builder().setHeader("Id", "Titulo", "Descrição", "Passos").build()
        );

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

        ByteArrayResource resource = new ByteArrayResource(out.toByteArray());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=tasks.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .contentLength(resource.contentLength())
                .body(resource);
    }

}