package team1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team1.domain.billing.MonthlyCharge;
import team1.domain.restaurant.Restaurant;

import java.util.Optional;
import java.util.UUID;

public interface MonthlyChargeRepository extends JpaRepository<MonthlyCharge, UUID> {
    Optional<MonthlyCharge> findByRestaurantAndChargeMonth(Restaurant restaurant, String chargeMonth);
}
