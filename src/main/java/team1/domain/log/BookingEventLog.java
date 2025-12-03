package team1.domain.log;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingEventLog {
    private String id;
    private String bookingId;
    private String actorUserId;
    private String eventType;
    private String prevState;
    private String newState;
    private String description;
    private LocalDateTime createdAt;
}
