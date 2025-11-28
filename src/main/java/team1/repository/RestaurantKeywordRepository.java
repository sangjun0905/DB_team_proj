package team1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team1.domain.restaurant.keyword.RestaurantKeyword;

import java.util.Optional;
import java.util.UUID;

public interface RestaurantKeywordRepository extends JpaRepository<RestaurantKeyword, UUID> {
    Optional<RestaurantKeyword> findByKeyword(String keyword);
}
