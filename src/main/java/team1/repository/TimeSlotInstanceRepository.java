package team1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team1.domain.timeslot.TimeslotInstance;

import java.time.LocalDate;
import java.util.List;

public interface TimeSlotInstanceRepository extends JpaRepository<TimeslotInstance, String> {
    List<TimeslotInstance> findByRuleIdAndDate(String ruleId, LocalDate date);
}
