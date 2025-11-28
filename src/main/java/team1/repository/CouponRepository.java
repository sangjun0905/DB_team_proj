package team1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team1.domain.user.User;
import team1.domain.user.asset.Coupon;

import java.util.List;
import java.util.UUID;

public interface CouponRepository extends JpaRepository<Coupon, UUID> {
    List<Coupon> findByUser(User user);
}
