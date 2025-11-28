package team1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team1.domain.code.CityCode;
import team1.domain.restaurant.Restaurant;
import team1.domain.user.User;

import java.util.List;
import java.util.UUID;

public interface RestaurantRepository extends JpaRepository<Restaurant, UUID> {
    List<Restaurant> findByOwner(User owner);
    List<Restaurant> findByCityCode(CityCode cityCode);
}
