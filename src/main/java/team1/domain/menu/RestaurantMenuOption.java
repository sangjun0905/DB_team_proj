package team1.domain.menu;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RestaurantMenuOption {
    private String id;
    private String optionGroupId;
    private String name;
    private int price;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
