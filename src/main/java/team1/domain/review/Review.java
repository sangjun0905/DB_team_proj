package team1.domain.review;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Review {
    private String id;
    private String userId;
    private String bookingId;
    private byte rating;
    private String content;
    private String embeddingVector;
    private boolean isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}
