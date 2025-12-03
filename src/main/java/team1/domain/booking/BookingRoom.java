package team1.domain.booking;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingRoom {
    private String id;
    private String bookingId;
    private String roomId;
    private int persons;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
