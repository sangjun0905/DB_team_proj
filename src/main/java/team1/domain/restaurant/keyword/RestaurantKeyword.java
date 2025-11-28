package team1.domain.restaurant.keyword;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team1.domain.common.BaseEntity;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "restaurant_keyword")
public class RestaurantKeyword extends BaseEntity {

    @Column(name = "keyword", length = 255)
    private String keyword;

    @OneToMany(mappedBy = "keyword")
    private List<RestaurantKeywordMapping> mappings = new ArrayList<>();

    public RestaurantKeyword(String keyword) {
        this.keyword = keyword;
    }
}
