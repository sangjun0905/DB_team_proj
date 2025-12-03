package team1.domain.restaurant;

import lombok.Data;
import team1.domain.common.RestaurantStaffRole;

import java.time.LocalDateTime;

@Data
public class RestaurantUserRole {
    private String id;
    private String restaurantId;
    private String userId;
    private RestaurantStaffRole role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
