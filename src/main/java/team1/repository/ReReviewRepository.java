package team1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team1.domain.review.ReReview;
import team1.domain.review.Review;

import java.util.List;
import java.util.UUID;

public interface ReReviewRepository extends JpaRepository<ReReview, UUID> {
    List<ReReview> findByReview(Review review);
}
