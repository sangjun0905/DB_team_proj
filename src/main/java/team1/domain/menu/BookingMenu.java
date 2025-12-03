package team1.domain.menu;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingMenu {
    private String id;
    private String bookingId;
    private String menuId;
    private int quantity;
    private int basePrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
