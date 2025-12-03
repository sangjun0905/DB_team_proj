package team1.domain.notice;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Notice {
    private String id;
    private String restaurantId;
    private String userId;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
