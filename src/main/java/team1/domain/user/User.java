package team1.domain.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team1.domain.common.BaseEntity;
import team1.domain.favorite.Favorite;
import team1.domain.notice.Notice;
import team1.domain.report.UserReportReview;
import team1.domain.report.UserReportRestaurant;
import team1.domain.reservation.Reservation;
import team1.domain.restaurant.RestaurantReportUser;
import team1.domain.review.ReReview;
import team1.domain.review.Review;
import team1.domain.review.ReviewLike;
import team1.domain.user.asset.Coupon;
import team1.domain.user.asset.UserPicture;
import team1.domain.user.asset.UserRecentView;
import team1.domain.user.asset.UserRestaurantBan;
import team1.domain.waiting.Waiting;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "`user`")
public class User extends BaseEntity {

    @Column(name = "email", length = 255)
    private String email;

    @Column(name = "birth", length = 255)
    private String birth;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Reservation> reservations = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Waiting> waitingEntries = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Favorite> favorites = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<UserPicture> pictures = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<UserRecentView> recentViews = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<UserRestaurantBan> bans = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Coupon> coupons = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<ReReview> reReviews = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<ReviewLike> reviewLikes = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Notice> notices = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<UserReportRestaurant> reportsToRestaurant = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<UserReportReview> reportsToReview = new ArrayList<>();

    @OneToMany(mappedBy = "reportedUser", fetch = FetchType.LAZY)
    private List<RestaurantReportUser> reportedByRestaurants = new ArrayList<>();

    public User(String email, String birth) {
        this.email = email;
        this.birth = birth;
    }
}
