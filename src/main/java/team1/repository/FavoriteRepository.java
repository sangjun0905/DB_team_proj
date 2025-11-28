package team1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team1.domain.favorite.Favorite;
import team1.domain.restaurant.Restaurant;
import team1.domain.user.User;

import java.util.Optional;
import java.util.UUID;

public interface FavoriteRepository extends JpaRepository<Favorite, UUID> {
    Optional<Favorite> findByUserAndRestaurant(User user, Restaurant restaurant);
    boolean existsByUserAndRestaurant(User user, Restaurant restaurant);
}
