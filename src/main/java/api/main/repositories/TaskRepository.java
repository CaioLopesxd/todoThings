package api.main.repositories;

import api.main.models.Task;
import api.main.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Integer> {
    List<Task> findByUser(User user);
    Optional<Task> findByIdAndUser(int id, User user);
}
