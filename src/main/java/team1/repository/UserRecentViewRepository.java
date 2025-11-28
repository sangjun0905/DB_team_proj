package team1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team1.domain.user.User;
import team1.domain.user.asset.UserRecentView;

import java.util.List;
import java.util.UUID;

public interface UserRecentViewRepository extends JpaRepository<UserRecentView, UUID> {
    List<UserRecentView> findByUser(User user);
}
