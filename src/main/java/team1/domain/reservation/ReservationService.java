package team1.domain.reservation;

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

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "reservation_service")
public class ReservationService extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    @Column(name = "`Key`", nullable = false, length = 255)
    private String serviceKey;

    @Column(name = "service_id", length = 255)
    private String serviceId;

    @Column(name = "cost", length = 255)
    private String cost;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "service_cnt")
    private Integer serviceCount;

    public ReservationService(Reservation reservation, String serviceKey) {
        this.reservation = reservation;
        this.serviceKey = serviceKey;
    }
}
