package team1.domain.review;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewLike {
    private String id;
    private String userId;
    private String reviewId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
