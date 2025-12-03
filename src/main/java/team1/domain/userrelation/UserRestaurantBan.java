package team1.domain.userrelation;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserRestaurantBan {
    private String id;
    private String userId;
    private String restaurantId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
