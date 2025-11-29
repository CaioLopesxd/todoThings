package api.main.repositories;

import api.main.models.TaskStep;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskStepRepository extends JpaRepository<TaskStep, Integer> {
    Optional<TaskStep> findByIdAndTaskId(int id, int taskId);
}
