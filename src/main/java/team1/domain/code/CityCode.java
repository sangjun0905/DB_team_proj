package team1.domain.code;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team1.domain.common.BaseEntity;
import team1.domain.restaurant.Restaurant;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "city_code",
        indexes = {
                @Index(name = "uk_city_code", columnList = "city_code", unique = true)
        }
)
public class CityCode extends BaseEntity {

    @Column(name = "city_code", length = 255)
    private String code;

    @OneToMany(mappedBy = "cityCode", fetch = FetchType.LAZY)
    private List<DistrictCode> districts = new ArrayList<>();

    @OneToMany(mappedBy = "cityCode", fetch = FetchType.LAZY)
    private List<Restaurant> restaurants = new ArrayList<>();

    public CityCode(String code) {
        this.code = code;
    }
}
