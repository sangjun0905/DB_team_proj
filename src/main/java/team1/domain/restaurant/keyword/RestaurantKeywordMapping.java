package team1.domain.restaurant.keyword;

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
import team1.domain.user.asset.UserRecentView;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "restaurant_keyword_mapping_table")
public class RestaurantKeywordMapping extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "keyword_id", nullable = false)
    private RestaurantKeyword keyword;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_recent_view_id", nullable = false)
    private UserRecentView recentView;

    public RestaurantKeywordMapping(RestaurantKeyword keyword, Restaurant restaurant, UserRecentView recentView) {
        this.keyword = keyword;
        this.restaurant = restaurant;
        this.recentView = recentView;
    }
}
