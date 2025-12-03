package team1.domain.restaurant;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RestaurantRoom {
    private String id;
    private String restaurantId;
    private String name;
    private int capacity;
    private String roomType;
    private boolean isActive;
    private String description;
    private boolean isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}
