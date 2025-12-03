package team1.domain.notification;

import lombok.Data;
import team1.domain.common.NotificationStatus;

import java.time.LocalDateTime;

@Data
public class Notification {
    private String id;
    private String userId;
    private String type;
    private String title;
    private String message;
    private NotificationStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime sentAt;
    private LocalDateTime readAt;
}
