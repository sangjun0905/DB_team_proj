package team1.domain.restaurant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team1.domain.common.BaseEntity;
import team1.domain.reservation.ReservationRoom;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "restaurant_room",
        indexes = {
                @Index(name = "idx_restaurant_room", columnList = "restaurant_id")
        }
)
public class RestaurantRoom extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @Column(name = "name", length = 255)
    private String name;

    @Column(name = "capacity")
    private Integer capacity;

    @Column(name = "room_type", length = 255)
    private String roomType;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    @Column(name = "description", length = 255)
    private String description;

    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY)
    private List<ReservationRoom> reservations = new ArrayList<>();

    public RestaurantRoom(Restaurant restaurant, String name, Integer capacity, String roomType, String description) {
        this.restaurant = restaurant;
        this.name = name;
        this.capacity = capacity;
        this.roomType = roomType;
        this.description = description;
    }
}
