package team1.domain.keyword;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RestaurantKeyword {
    private String id;
    private String keyword;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
