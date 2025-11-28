package team1.domain.restaurant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team1.domain.common.BaseEntity;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "restaurant_timeslot_rule")
public class RestaurantTimeSlotRule extends BaseEntity {

    @Column(name = "day_of_week")
    private Integer dayOfWeek;

    @Column(name = "open_time")
    private LocalTime openTime;

    @Column(name = "close_time")
    private LocalTime closeTime;

    @Column(name = "slot_interval")
    private Integer slotIntervalMinutes;

    @Column(name = "usage_time")
    private Integer usageTimeMinutes;

    @Column(name = "team_capacity")
    private Integer teamCapacity;

    @Column(name = "allow_waiting")
    private Boolean allowWaiting;

    @Column(name = "is_holiday")
    private Boolean holiday;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @OneToMany(mappedBy = "timeSlotRule", fetch = FetchType.LAZY)
    private List<TimeSlotInstance> instances = new ArrayList<>();

    public RestaurantTimeSlotRule(
            Restaurant restaurant,
            Integer dayOfWeek,
            LocalTime openTime,
            LocalTime closeTime,
            Integer slotIntervalMinutes,
            Integer usageTimeMinutes,
            Integer teamCapacity,
            Boolean allowWaiting,
            Boolean holiday
    ) {
        this.restaurant = restaurant;
        this.dayOfWeek = dayOfWeek;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.slotIntervalMinutes = slotIntervalMinutes;
        this.usageTimeMinutes = usageTimeMinutes;
        this.teamCapacity = teamCapacity;
        this.allowWaiting = allowWaiting;
        this.holiday = holiday;
    }
}
