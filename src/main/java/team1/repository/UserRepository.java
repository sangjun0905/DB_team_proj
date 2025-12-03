package team1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team1.domain.user.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
}
