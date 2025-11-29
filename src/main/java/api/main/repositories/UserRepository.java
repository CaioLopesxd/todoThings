package api.main.repositories;

import api.main.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    boolean existsByIdAndContacts_Id(UUID ownerId, UUID contactId);
}
