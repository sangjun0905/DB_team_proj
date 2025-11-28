package team1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team1.domain.user.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
}
