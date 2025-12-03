package team1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team1.domain.keyword.RestaurantKeyword;

import java.util.Optional;

public interface RestaurantKeywordRepository extends JpaRepository<RestaurantKeyword, String> {
    Optional<RestaurantKeyword> findByKeyword(String keyword);
}
