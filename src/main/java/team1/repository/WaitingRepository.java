package team1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team1.domain.restaurant.Restaurant;
import team1.domain.waiting.Waiting;

import java.util.List;
import java.util.UUID;

public interface WaitingRepository extends JpaRepository<Waiting, UUID> {
    List<Waiting> findByRestaurantOrderByWaitingOrder(Restaurant restaurant);
}
