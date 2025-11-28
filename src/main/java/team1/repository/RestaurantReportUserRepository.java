package team1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team1.domain.restaurant.Restaurant;
import team1.domain.restaurant.RestaurantReportUser;
import team1.domain.user.User;

import java.util.List;
import java.util.UUID;

public interface RestaurantReportUserRepository extends JpaRepository<RestaurantReportUser, UUID> {
    List<RestaurantReportUser> findByRestaurant(Restaurant restaurant);
    List<RestaurantReportUser> findByReportedUser(User user);
}
