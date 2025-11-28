package team1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team1.domain.code.CityCode;
import team1.domain.code.DistrictCode;

import java.util.List;
import java.util.UUID;

public interface DistrictCodeRepository extends JpaRepository<DistrictCode, UUID> {
    List<DistrictCode> findByCityCode(CityCode cityCode);
}
