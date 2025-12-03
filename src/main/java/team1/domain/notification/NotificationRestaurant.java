package team1.domain.notification;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationRestaurant {
    private String notificationId;
    private String restaurantId;
    private LocalDateTime createdAt;
}
