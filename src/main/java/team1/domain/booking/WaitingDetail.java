package team1.domain.booking;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WaitingDetail {
    private String bookingId;
    private LocalDateTime waitingStartTime;
    private LocalDateTime expectedStartTime;
    private Integer expectedWaitingMinute;
    private Integer queueOrder;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
