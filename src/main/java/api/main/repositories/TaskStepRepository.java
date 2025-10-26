package api.main.repositories;

import api.main.models.TaskStep;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskStepRepository extends JpaRepository<TaskStep, Integer> {
}
