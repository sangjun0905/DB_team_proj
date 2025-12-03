package team1.domain.coupon;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Coupon {
    private String id;
    private String userId;
    private String templateId;
    private boolean isUsed;
    private LocalDateTime usedAt;
    private boolean isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}
