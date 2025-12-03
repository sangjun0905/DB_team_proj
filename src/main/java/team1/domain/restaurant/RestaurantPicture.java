package team1.domain.restaurant;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RestaurantPicture {
    private String id;
    private String restaurantId;
    private String pictureUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
