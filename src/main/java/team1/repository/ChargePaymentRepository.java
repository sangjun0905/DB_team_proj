package team1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team1.domain.billing.ChargePayment;
import team1.domain.billing.MonthlyCharge;

import java.util.List;
import java.util.UUID;

public interface ChargePaymentRepository extends JpaRepository<ChargePayment, UUID> {
    List<ChargePayment> findByMonthlyCharge(MonthlyCharge monthlyCharge);
}
