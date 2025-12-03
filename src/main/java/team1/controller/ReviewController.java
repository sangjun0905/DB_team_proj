package team1.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team1.domain.review.Review;
import team1.dto.review.ReviewCreateRequest;
import team1.service.review.ReviewService;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody ReviewCreateRequest req) throws SQLException {
        return ResponseEntity.ok(reviewService.createReview(req));
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<Review>> listByRestaurant(@PathVariable String restaurantId) throws SQLException {
        return ResponseEntity.ok(reviewService.findByRestaurant(restaurantId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Review>> listByUser(@PathVariable String userId) throws SQLException {
        return ResponseEntity.ok(reviewService.findByUser(userId));
    }
}
