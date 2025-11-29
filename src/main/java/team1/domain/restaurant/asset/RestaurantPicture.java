package team1.domain.restaurant.asset;

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

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "restaurant_picture")
public class RestaurantPicture extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @Column(name = "restaurant_picture", length = 255)
    private String pictureUrl;

    public RestaurantPicture(Restaurant restaurant, String pictureUrl) {
        this.restaurant = restaurant;
        this.pictureUrl = pictureUrl;
    }
}
