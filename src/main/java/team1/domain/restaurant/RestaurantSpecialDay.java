package team1.domain.restaurant;

import lombok.Data;
import team1.domain.common.BaseEntity;

import java.time.LocalDate;

@Data
@lombok.EqualsAndHashCode(callSuper = true)
public class RestaurantSpecialDay extends BaseEntity {
    private String restaurantId;
    private LocalDate date;
    private RestaurantSpecialDayType type;
    private boolean allowReservation;
    private boolean allowWaiting;
    private String memo;
}
