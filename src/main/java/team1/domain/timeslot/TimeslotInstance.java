package team1.domain.timeslot;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class TimeslotInstance {
    private String id;
    private String ruleId;
    private LocalDate date;
    private LocalTime startTime;
    private int teamCapacity;
    private int reservedTeam;
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
