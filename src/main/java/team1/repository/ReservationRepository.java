package team1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team1.domain.reservation.Reservation;
import team1.domain.restaurant.Restaurant;
import team1.domain.user.User;

import java.util.List;
import java.util.UUID;

public interface ReservationRepository extends JpaRepository<Reservation, UUID> {
    List<Reservation> findByUser(User user);
    List<Reservation> findByRestaurant(Restaurant restaurant);
}
