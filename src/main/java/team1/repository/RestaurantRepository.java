package team1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team1.domain.restaurant.Restaurant;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, String> {
    List<Restaurant> findByCityId(String cityId);
}
