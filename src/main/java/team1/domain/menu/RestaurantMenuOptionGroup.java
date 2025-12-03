package team1.domain.menu;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RestaurantMenuOptionGroup {
    private String id;
    private String menuId;
    private String name;
    private boolean isRequired;
    private int maxSelect;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
