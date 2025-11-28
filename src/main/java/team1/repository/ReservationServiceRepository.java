package team1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team1.domain.reservation.Reservation;
import team1.domain.reservation.ReservationService;

import java.util.List;
import java.util.UUID;

public interface ReservationServiceRepository extends JpaRepository<ReservationService, UUID> {
    List<ReservationService> findByReservation(Reservation reservation);
}
