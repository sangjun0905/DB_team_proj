package team1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team1.domain.timeslot.RestaurantTimeslotRule;

import java.util.List;

public interface RestaurantTimeSlotRuleRepository extends JpaRepository<RestaurantTimeslotRule, String> {
    List<RestaurantTimeslotRule> findByRestaurantIdAndDayOfWeek(String restaurantId, Integer dayOfWeek);
}
