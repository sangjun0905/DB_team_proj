package team1.domain.userrelation;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Favorites {
    private String id;
    private String userId;
    private String restaurantId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
