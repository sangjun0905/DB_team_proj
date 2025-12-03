package team1.domain.review;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewPicture {
    private String id;
    private String reviewId;
    private String pictureUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
