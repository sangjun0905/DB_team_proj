package team1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team1.domain.notice.Notice;

import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, String> {
    List<Notice> findByRestaurantId(String restaurantId);
    List<Notice> findByUserId(String userId);
}
