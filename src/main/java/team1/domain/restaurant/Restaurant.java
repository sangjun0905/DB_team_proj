package team1.domain.restaurant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team1.domain.billing.MonthlyCharge;
import team1.domain.code.CityCode;
import team1.domain.common.BaseEntity;
import team1.domain.favorite.Favorite;
import team1.domain.notice.Notice;
import team1.domain.reservation.Reservation;
import team1.domain.reservation.ReservationSlot;
import team1.domain.restaurant.asset.RestaurantPicture;
import team1.domain.restaurant.RestaurantReportUser;
import team1.domain.restaurant.keyword.RestaurantKeywordMapping;
import team1.domain.review.Review;
import team1.domain.user.User;
import team1.domain.user.asset.UserRecentView;
import team1.domain.user.asset.UserRestaurantBan;
import team1.domain.waiting.Waiting;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "restaurant",
        indexes = {
                @Index(name = "idx_restaurant_owner", columnList = "user_id"),
                @Index(name = "idx_restaurant_city", columnList = "city_code_id")
        }
)
public class Restaurant extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;

    // 원본 DDL의 Field/Field2는 의미가 불분명하여 안전하게 그대로 매핑한다.
    @Column(name = "Field")
    private Float rating;

    @Column(name = "cnt_review")
    private Integer reviewCount;

    @Column(name = "Field2")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "city_code_id", nullable = false)
    private CityCode cityCode;

    @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY)
    private List<RestaurantRoom> rooms = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY)
    private List<RestaurantTimeSlotRule> timeSlotRules = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY)
    private List<Reservation> reservations = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY)
    private List<ReservationSlot> reservationSlots = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY)
    private List<Waiting> waitings = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY)
    private List<Favorite> favorites = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY)
    private List<UserRecentView> recentViews = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY)
    private List<UserRestaurantBan> bans = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY)
    private List<RestaurantPicture> pictures = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY)
    private List<RestaurantKeywordMapping> keywordMappings = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY)
    private List<Notice> notices = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY)
    private List<MonthlyCharge> monthlyCharges = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY)
    private List<RestaurantReportUser> reportedUsers = new ArrayList<>();

    public Restaurant(User owner, CityCode cityCode, Float rating, Integer reviewCount, String description) {
        this.owner = owner;
        this.cityCode = cityCode;
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.description = description;
    }
}
