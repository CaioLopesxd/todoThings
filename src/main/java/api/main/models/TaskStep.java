package api.main.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "task_steps")
public class TaskStep {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    @JsonBackReference
    private Task task;

    @Column
    private String description;

    public TaskStatus getStepStatus() {
        return stepStatus;
    }

    public void setStepStatus(TaskStatus stepStatus) {
        this.stepStatus = stepStatus;
    }

    @Enumerated(EnumType.STRING)
    private TaskStatus stepStatus;

    public TaskStep() {}
    public TaskStep(Task task, String description, TaskStatus stepStatus) {
        this.task = task;
        this.description = description;
        this.stepStatus = stepStatus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
