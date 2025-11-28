package team1.domain.reservation;

import jakarta.persistence.Column;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team1.domain.common.BaseEntity;
import team1.domain.restaurant.RestaurantRoom;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "reservation_room",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_reservation_room", columnNames = {"reservation_id", "room_id"})
        }
)
public class ReservationRoom extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "room_id", nullable = false)
    private RestaurantRoom room;

    @Column(name = "persons")
    private Integer persons;

    public ReservationRoom(Reservation reservation, RestaurantRoom room, Integer persons) {
        this.reservation = reservation;
        this.room = room;
        this.persons = persons;
    }
}
