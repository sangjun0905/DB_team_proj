package team1.domain.restaurant;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Restaurant {
    private String id;
    private String name;
    private String cityId;
    private String districtId;
    private String address;
    private String phone;
    private boolean supportsReservation;
    private boolean supportsWaiting;
    private BigDecimal avgRating;
    private int reviewCount;
    private boolean isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}
