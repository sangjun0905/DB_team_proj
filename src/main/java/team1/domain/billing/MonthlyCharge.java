package team1.domain.billing;

import lombok.Data;
import team1.domain.common.MonthlyChargeStatus;

import java.time.LocalDateTime;

@Data
public class MonthlyCharge {
    private String id;
    private String restaurantId;
    private String chargeMonth;
    private int totalUsageCnt;
    private int totalAdCnt;
    private int totalCharge;
    private int chargePerReservation;
    private MonthlyChargeStatus status;
    private boolean isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}
