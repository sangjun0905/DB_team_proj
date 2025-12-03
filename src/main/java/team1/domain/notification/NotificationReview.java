package team1.domain.notification;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationReview {
    private String notificationId;
    private String reviewId;
    private LocalDateTime createdAt;
}
