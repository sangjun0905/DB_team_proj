package team1.domain.reservation;

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
import team1.domain.restaurant.Restaurant;
import team1.domain.review.ReReview;
import team1.domain.review.Review;
import team1.domain.user.User;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "reservation")
public class Reservation extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "cost", length = 255)
    private String cost;

    @Column(name = "state", length = 255)
    private String state;

    @Column(name = "persons", length = 255)
    private String persons;

    @Column(name = "date", length = 255)
    private String date;

    @Column(name = "start_time", length = 255)
    private String startTime;

    @Column(name = "end_time", length = 255)
    private String endTime;

    @Column(name = "usage_time", length = 255)
    private String usageTime;

    @OneToMany(mappedBy = "reservation", fetch = FetchType.LAZY)
    private List<ReservationRoom> reservationRooms = new ArrayList<>();

    @OneToMany(mappedBy = "reservation", fetch = FetchType.LAZY)
    private List<ReservationService> services = new ArrayList<>();

    @OneToMany(mappedBy = "reservation", fetch = FetchType.LAZY)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "reservation", fetch = FetchType.LAZY)
    private List<ReReview> reReviews = new ArrayList<>();

    public Reservation(Restaurant restaurant, User user) {
        this.restaurant = restaurant;
        this.user = user;
    }
}
