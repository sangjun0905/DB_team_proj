package team1.domain.restaurant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team1.domain.common.BaseEntity;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "timeslot_instance")
public class TimeSlotInstance extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "timeslot_rule_id", nullable = false)
    private RestaurantTimeSlotRule timeSlotRule;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "team_capacity")
    private Integer teamCapacity;

    @Column(name = "reserved_team")
    private Integer reservedTeam;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    public TimeSlotInstance(RestaurantTimeSlotRule timeSlotRule, LocalDate date, LocalTime startTime, Integer teamCapacity) {
        this.timeSlotRule = timeSlotRule;
        this.date = date;
        this.startTime = startTime;
        this.teamCapacity = teamCapacity;
        this.reservedTeam = 0;
    }
}
