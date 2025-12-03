package team1.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team1.dao.StatsDao;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/stats")
public class StatsController {

    private final StatsDao statsDao;

    public StatsController(StatsDao statsDao) {
        this.statsDao = statsDao;
    }

    @GetMapping("/restaurants/{restaurantId}/room-capacity")
    public ResponseEntity<Map<String, Integer>> restaurantCapacity(@PathVariable String restaurantId) throws SQLException {
        int value = statsDao.getRestaurantTotalRoomCapacity(restaurantId);
        Map<String, Integer> body = new java.util.HashMap<>();
        body.put("totalRoomCapacity", value);
        // restaurantId는 문자열이라 별도 응답 항목으로 내려주려면 Object 맵을 쓰는 게 안전합니다.
        return ResponseEntity.ok(body);
    }

    @GetMapping("/restaurants/{restaurantId}/occupancy")
    public ResponseEntity<Map<String, Object>> occupancyRate(@PathVariable String restaurantId,
                                                             @RequestParam("date") String date) throws SQLException {
        LocalDate d = LocalDate.parse(date);
        BigDecimal rate = statsDao.getRestaurantDailyOccupancyRate(restaurantId, d);
        Map<String, Object> body = new java.util.HashMap<>();
        body.put("restaurantId", restaurantId);
        body.put("date", d.toString());
        body.put("occupancyRate", rate);
        return ResponseEntity.ok(body);
    }

    @GetMapping("/restaurants/{restaurantId}/noshow")
    public ResponseEntity<Map<String, Integer>> noshowCount(@PathVariable String restaurantId,
                                                            @RequestParam("userId") String userId) throws SQLException {
        int value = statsDao.getUserNoshowCount(userId, restaurantId);
        Map<String, Integer> body = new java.util.HashMap<>();
        body.put("noshowCount", value);
        return ResponseEntity.ok(body);
    }
}
