package team1.domain.menu;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingMenuOption {
    private String id;
    private String bookingMenuId;
    private String optionId;
    private int price;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
