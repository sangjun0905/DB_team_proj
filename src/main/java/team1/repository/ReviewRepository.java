package team1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team1.domain.review.Review;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, String> {
    List<Review> findByUserId(String userId);
    List<Review> findByBookingId(String bookingId);
}
