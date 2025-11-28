package team1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team1.domain.notice.Notice;
import team1.domain.restaurant.Restaurant;
import team1.domain.user.User;

import java.util.List;
import java.util.UUID;

public interface NoticeRepository extends JpaRepository<Notice, UUID> {
    List<Notice> findByRestaurant(Restaurant restaurant);
    List<Notice> findByUser(User user);
}
