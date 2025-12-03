package team1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team1.domain.coupon.Coupon;

import java.util.List;

public interface CouponRepository extends JpaRepository<Coupon, String> {
    List<Coupon> findByUserId(String userId);
}
