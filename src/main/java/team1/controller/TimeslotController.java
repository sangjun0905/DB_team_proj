package team1.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team1.service.timeslot.TimeslotGenerationService;
import team1.dto.timeslot.TimeslotGenerateRequest;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/timeslots")
public class TimeslotController {

    private final TimeslotGenerationService generationService;

    public TimeslotController(TimeslotGenerationService generationService) {
        this.generationService = generationService;
    }

    /**
     * 특정 매장에 대해 startDate부터 days일 동안 timeslot_instance를 생성.
     * 이미 존재하는 슬롯은 건너뜁니다.
     */
    @PostMapping("/generate")
    public ResponseEntity<Map<String, Object>> generate(@RequestParam String restaurantId,
                                                        @RequestParam String startDate,
                                                        @RequestParam(defaultValue = "30") int days) throws Exception {
        LocalDate start = LocalDate.parse(startDate);
        int created = generationService.generate(restaurantId, start, days);
        return ResponseEntity.ok(Map.of(
                "restaurantId", restaurantId,
                "startDate", start.toString(),
                "days", days,
                "createdCount", created
        ));
    }

    /**
     * 바디로도 생성할 수 있게 대안 엔드포인트 추가.
     */
    @PostMapping("/generate-body")
    public ResponseEntity<Map<String, Object>> generateByBody(@RequestBody TimeslotGenerateRequest req) throws Exception {
        String restaurantId = req.getRestaurantId();
        LocalDate start = LocalDate.parse(req.getStartDate());
        int days = req.getDays() == null ? 30 : req.getDays();
        int created = generationService.generate(restaurantId, start, days);
        return ResponseEntity.ok(Map.of(
                "restaurantId", restaurantId,
                "startDate", start.toString(),
                "days", days,
                "createdCount", created
        ));
    }
}
