package team1.domain.billing;

import lombok.Data;
import team1.domain.common.BillingEventType;

import java.time.LocalDateTime;

@Data
public class BillingEventLog {
    private String id;
    private String monthlyChargeId;
    private String bookingId;
    private int amount;
    private BillingEventType eventType;
    private LocalDateTime createdAt;
}
