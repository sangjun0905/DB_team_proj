package team1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team1.domain.restaurant.Restaurant;
import team1.domain.review.Review;
import team1.domain.user.User;

import java.util.List;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {
    List<Review> findByRestaurant(Restaurant restaurant);
    List<Review> findByUser(User user);
}
