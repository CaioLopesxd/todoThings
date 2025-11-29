package api.main.repositories;

import api.main.models.Task;
import api.main.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Integer> {
    Optional<Task> findByIdAndTaskOwner(int id, User taskOwner);
    List<Task> findByAssignedUsersContains(User user);
    Optional<Task> findByIdAndAssignedUsersContains(int id, User user);
}

