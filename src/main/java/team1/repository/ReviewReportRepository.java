package team1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team1.domain.review.Review;
import team1.domain.review.ReviewReport;
import team1.domain.user.User;

import java.util.List;
import java.util.UUID;

public interface ReviewReportRepository extends JpaRepository<ReviewReport, UUID> {
    List<ReviewReport> findByReview(Review review);
    List<ReviewReport> findByUser(User user);
}
