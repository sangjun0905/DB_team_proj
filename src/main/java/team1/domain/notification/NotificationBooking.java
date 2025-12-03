package team1.domain.notification;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationBooking {
    private String notificationId;
    private String bookingId;
    private LocalDateTime createdAt;
}
