package team1.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team1.domain.timeslot.TimeslotInstance;
import team1.dto.restaurant.RestaurantRegistrationRequest;
import team1.service.restaurant.RestaurantRegistrationException;
import team1.service.restaurant.RestaurantRegistrationService;
import team1.dao.RestaurantDao;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private final RestaurantRegistrationService registrationService;
    private final RestaurantDao restaurantDao;

    public RestaurantController(RestaurantRegistrationService registrationService,
                                RestaurantDao restaurantDao) {
        this.registrationService = registrationService;
        this.restaurantDao = restaurantDao;
    }

    @PostMapping
    public ResponseEntity<String> register(@RequestBody RestaurantRegistrationRequest req)
            throws RestaurantRegistrationException {
        String id = registrationService.registerRestaurant(req);
        return ResponseEntity.ok(id);
    }

    @GetMapping
    public ResponseEntity<List<team1.domain.restaurant.Restaurant>> listAll() throws SQLException {
        return ResponseEntity.ok(restaurantDao.findAll());
    }

    @GetMapping("/{restaurantId}/timeslots")
    public ResponseEntity<List<TimeslotInstance>> listTimeslots(@PathVariable String restaurantId,
                                                                @RequestParam("date") String date) throws SQLException {
        LocalDate d = LocalDate.parse(date);
        return ResponseEntity.ok(restaurantDao.findTimeslots(restaurantId, d));
    }
}
