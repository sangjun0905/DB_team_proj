package team1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team1.domain.user.User;
import team1.domain.user.asset.UserRestaurantBan;
import team1.domain.restaurant.Restaurant;

import java.util.Optional;
import java.util.UUID;

public interface UserRestaurantBanRepository extends JpaRepository<UserRestaurantBan, UUID> {
    Optional<UserRestaurantBan> findByUserAndRestaurant(User user, Restaurant restaurant);
}
