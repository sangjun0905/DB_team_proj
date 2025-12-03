package team1.domain.menu;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RestaurantMenu {
    private String id;
    private String restaurantId;
    private String name;
    private int price;
    private String description;
    private String imageUrl;
    private int sortOrder;
    private boolean isActive;
    private boolean isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}
