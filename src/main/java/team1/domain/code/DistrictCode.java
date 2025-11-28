package team1.domain.code;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team1.domain.common.BaseEntity;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "district_code",
        indexes = {
                @Index(name = "idx_district_city", columnList = "city_code_id")
        }
)
public class DistrictCode extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "city_code_id", nullable = false)
    private CityCode cityCode;

    @Column(name = "district", length = 255)
    private String district;

    public DistrictCode(CityCode cityCode, String district) {
        this.cityCode = cityCode;
        this.district = district;
    }
}
