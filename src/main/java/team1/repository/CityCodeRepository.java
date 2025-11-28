package team1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team1.domain.code.CityCode;

import java.util.Optional;
import java.util.UUID;

public interface CityCodeRepository extends JpaRepository<CityCode, UUID> {
    Optional<CityCode> findByCode(String code);
}
