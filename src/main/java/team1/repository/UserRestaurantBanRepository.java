package team1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team1.domain.userrelation.UserRestaurantBan;

import java.util.Optional;

public interface UserRestaurantBanRepository extends JpaRepository<UserRestaurantBan, String> {
    Optional<UserRestaurantBan> findByUserIdAndRestaurantId(String userId, String restaurantId);
}
