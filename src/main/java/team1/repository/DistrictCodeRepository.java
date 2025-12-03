package team1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team1.domain.region.DistrictCode;

import java.util.List;

public interface DistrictCodeRepository extends JpaRepository<DistrictCode, String> {
    List<DistrictCode> findByCityId(String cityId);
}
