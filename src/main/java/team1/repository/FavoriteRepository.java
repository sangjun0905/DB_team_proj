package team1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team1.domain.userrelation.Favorites;

import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorites, String> {
    Optional<Favorites> findByUserIdAndRestaurantId(String userId, String restaurantId);
    boolean existsByUserIdAndRestaurantId(String userId, String restaurantId);
}
