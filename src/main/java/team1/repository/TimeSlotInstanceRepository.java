package team1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team1.domain.restaurant.RestaurantTimeSlotRule;
import team1.domain.restaurant.TimeSlotInstance;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface TimeSlotInstanceRepository extends JpaRepository<TimeSlotInstance, UUID> {
    List<TimeSlotInstance> findByTimeSlotRuleAndDate(RestaurantTimeSlotRule rule, LocalDate date);
}
