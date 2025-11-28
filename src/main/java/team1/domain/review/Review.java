package team1.domain.review;

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
import team1.domain.common.BaseEntity;
import team1.domain.reservation.Reservation;
import team1.domain.restaurant.Restaurant;
import team1.domain.user.User;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "review",
        indexes = {
                @Index(name = "idx_review_restaurant", columnList = "restaurant_id")
        }
)
public class Review extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    @Column(name = "review_point", length = 255)
    private String reviewPoint;

    @Column(name = "review_content", length = 200)
    private String reviewContent;

    @Column(name = "state", length = 255)
    private String state;

    @Column(name = "embedding_vector", length = 255)
    private String embeddingVector;

    @OneToMany(mappedBy = "review", fetch = FetchType.LAZY)
    private List<ReviewPicture> pictures = new ArrayList<>();

    @OneToMany(mappedBy = "review", fetch = FetchType.LAZY)
    private List<ReviewLike> likes = new ArrayList<>();

    @OneToMany(mappedBy = "review", fetch = FetchType.LAZY)
    private List<ReviewReport> reports = new ArrayList<>();

    @OneToMany(mappedBy = "review", fetch = FetchType.LAZY)
    private List<ReReview> reReviews = new ArrayList<>();

    public Review(Restaurant restaurant, User user, Reservation reservation, String reviewPoint, String reviewContent) {
        this.restaurant = restaurant;
        this.user = user;
        this.reservation = reservation;
        this.reviewPoint = reviewPoint;
        this.reviewContent = reviewContent;
    }
}
