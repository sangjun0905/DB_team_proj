package team1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team1.domain.restaurant.Restaurant;
import team1.domain.restaurant.RestaurantTimeSlotRule;

import java.util.List;
import java.util.UUID;

public interface RestaurantTimeSlotRuleRepository extends JpaRepository<RestaurantTimeSlotRule, UUID> {
    List<RestaurantTimeSlotRule> findByRestaurantAndDayOfWeek(Restaurant restaurant, Integer dayOfWeek);
}
