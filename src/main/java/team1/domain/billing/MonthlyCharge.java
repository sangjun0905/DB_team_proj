package team1.domain.billing;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
        name = "monthly_charge",
        uniqueConstraints = {
                @UniqueConstraint(name = "ux_monthly_charge_restaurant_month", columnNames = {"restaurant_id", "charge_month"})
        },
        indexes = {
                @Index(name = "idx_monthly_charge_restaurant", columnList = "restaurant_id")
        }
)
public class MonthlyCharge extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @Column(name = "charge_month", nullable = false, length = 7)
    private String chargeMonth;

    @Column(name = "total_ad_cnt_per_month")
    private Integer totalAdCountPerMonth;

    @Column(name = "total_usage_cnt_per_month")
    private Integer totalUsageCountPerMonth;

    @Column(name = "total_charge")
    private Float totalCharge;

    @Column(name = "charge_per_reservation")
    private Float chargePerReservation;

    @Column(name = "status", length = 255)
    private String status;

    @OneToMany(mappedBy = "monthlyCharge", fetch = FetchType.LAZY)
    private List<ChargePayment> payments = new ArrayList<>();

    public MonthlyCharge(Restaurant restaurant, String chargeMonth) {
        this.restaurant = restaurant;
        this.chargeMonth = chargeMonth;
    }
}
