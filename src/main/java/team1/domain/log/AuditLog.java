package team1.domain.log;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AuditLog {
    private String id;
    private String userId;
    private String action;
    private String ip;
    private String userAgent;
    private LocalDateTime createdAt;
}
