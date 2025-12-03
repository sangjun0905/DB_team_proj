package team1.domain.restaurant;

import lombok.Data;
import team1.domain.common.BaseEntity;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@lombok.EqualsAndHashCode(callSuper = true)
public class RestaurantBreakTime extends BaseEntity {
    private String restaurantId;
    private int dayOfWeek;
    private LocalTime breakStartTime;
    private LocalTime breakEndTime;
    private LocalDate applyDate;
    private boolean active;
    private String memo;
}
