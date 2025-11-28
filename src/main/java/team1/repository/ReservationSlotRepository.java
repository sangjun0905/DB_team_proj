package team1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team1.domain.reservation.ReservationSlot;
import team1.domain.restaurant.Restaurant;

import java.util.List;
import java.util.UUID;

public interface ReservationSlotRepository extends JpaRepository<ReservationSlot, UUID> {
    List<ReservationSlot> findByRestaurant(Restaurant restaurant);
}
