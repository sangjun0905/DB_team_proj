package team1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team1.domain.keyword.RestaurantKeyword;
import team1.domain.keyword.RestaurantKeywordMap;

import java.util.List;

public interface RestaurantKeywordMappingRepository extends JpaRepository<RestaurantKeywordMap, String> {
    List<RestaurantKeywordMap> findByRestaurantId(String restaurantId);
    List<RestaurantKeywordMap> findByKeywordId(String keywordId);
}
