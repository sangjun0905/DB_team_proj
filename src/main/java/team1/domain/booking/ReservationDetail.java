package team1.domain.booking;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservationDetail {
    private String bookingId;
    private String timeslotInstanceId;
    private Integer usageMinutes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
