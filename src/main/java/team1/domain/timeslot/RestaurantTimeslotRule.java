package team1.domain.timeslot;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class RestaurantTimeslotRule {
    private String id;
    private String restaurantId;
    private int dayOfWeek;
    private LocalTime openTime;
    private LocalTime closeTime;
    private int slotInterval;
    private int usageTime;
    private int teamCapacity;
    private boolean isHoliday;
    private boolean allowWaiting;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
