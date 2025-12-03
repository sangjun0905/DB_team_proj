package team1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team1.domain.region.CityCode;

import java.util.Optional;

public interface CityCodeRepository extends JpaRepository<CityCode, String> {
    Optional<CityCode> findByCityName(String cityName);
}
