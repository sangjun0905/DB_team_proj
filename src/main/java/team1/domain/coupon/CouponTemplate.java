package team1.domain.coupon;

import lombok.Data;
import team1.domain.common.DiscountType;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class CouponTemplate {
    private String id;
    private String name;
    private DiscountType discountType;
    private int discountValue;
    private int minOrderPrice;
    private Integer maxDiscount;
    private LocalDate validFrom;
    private LocalDate validUntil;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
