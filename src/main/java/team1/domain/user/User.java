package team1.domain.user;

import lombok.Data;
import team1.domain.common.UserRole;

import java.time.LocalDateTime;

@Data
public class User {
    private String id;
    private String email;
    private String passwordHash;
    private String name;
    private String phone;
    private UserRole role;
    private boolean isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}
