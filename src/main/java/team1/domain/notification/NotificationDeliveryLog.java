package team1.domain.notification;

import lombok.Data;
import team1.domain.common.NotificationChannel;
import team1.domain.common.NotificationDeliveryStatus;

import java.time.LocalDateTime;

@Data
public class NotificationDeliveryLog {
    private String id;
    private String notificationId;
    private NotificationChannel channel;
    private NotificationDeliveryStatus status;
    private String failReason;
    private LocalDateTime createdAt;
}
