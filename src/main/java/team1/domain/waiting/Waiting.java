package team1.domain.waiting;

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
import team1.domain.restaurant.Restaurant;
import team1.domain.user.User;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "waiting")
public class Waiting extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "party_size")
    private Integer partySize;

    @Column(name = "usage_per_team")
    private Integer usagePerTeam;

    @Column(name = "waiting_start_time")
    private LocalDateTime waitingStartTime;

    @Column(name = "expected_start_time")
    private LocalDateTime expectedStartTime;

    @Column(name = "expected_waiting_minute")
    private Integer expectedWaitingMinute;

    @Column(name = "`order`")
    private Integer waitingOrder;

    public Waiting(Restaurant restaurant, User user, Integer partySize) {
        this.restaurant = restaurant;
        this.user = user;
        this.partySize = partySize;
    }
}
