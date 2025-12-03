package team1.domain.keyword;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RestaurantKeywordMap {
    private String id;
    private String restaurantId;
    private String keywordId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
