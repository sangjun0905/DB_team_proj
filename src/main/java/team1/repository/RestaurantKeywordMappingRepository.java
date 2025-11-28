package team1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team1.domain.restaurant.Restaurant;
import team1.domain.restaurant.keyword.RestaurantKeyword;
import team1.domain.restaurant.keyword.RestaurantKeywordMapping;

import java.util.List;
import java.util.UUID;

public interface RestaurantKeywordMappingRepository extends JpaRepository<RestaurantKeywordMapping, UUID> {
    List<RestaurantKeywordMapping> findByRestaurant(Restaurant restaurant);
    List<RestaurantKeywordMapping> findByKeyword(RestaurantKeyword keyword);
}
