package team1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team1.domain.billing.MonthlyCharge;

import java.util.Optional;

public interface MonthlyChargeRepository extends JpaRepository<MonthlyCharge, String> {
    Optional<MonthlyCharge> findByRestaurantIdAndChargeMonth(String restaurantId, String chargeMonth);
}
