package team1.domain.billing;

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

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "is_charged")
public class ChargePayment extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "monthly_charge_id", nullable = false)
    private MonthlyCharge monthlyCharge;

    @Column(name = "is_paid")
    private Boolean paid;

    @Column(name = "paid_time")
    private LocalDateTime paidTime;

    @Column(name = "total_charge")
    private Float totalCharge;

    public ChargePayment(MonthlyCharge monthlyCharge, Boolean paid, LocalDateTime paidTime, Float totalCharge) {
        this.monthlyCharge = monthlyCharge;
        this.paid = paid;
        this.paidTime = paidTime;
        this.totalCharge = totalCharge;
    }
}
