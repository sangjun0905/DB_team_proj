package team1.service.review;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team1.dao.ReviewDao;
import team1.domain.review.Review;
import team1.dto.review.ReviewCreateRequest;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@Service
public class ReviewService {

    private final ReviewDao reviewDao;

    public ReviewService(ReviewDao reviewDao) {
        this.reviewDao = reviewDao;
    }

    @Transactional(rollbackFor = Exception.class)
    public String createReview(ReviewCreateRequest req) throws SQLException {
        Review review = new Review();
        review.setId(UUID.randomUUID().toString());
        review.setUserId(req.getUserId());
        review.setBookingId(req.getBookingId());
        review.setRating(req.getRating());
        review.setContent(req.getContent());
        review.setEmbeddingVector(req.getEmbeddingVector());
        reviewDao.insertReview(review);
        return review.getId();
    }

    public List<Review> findByRestaurant(String restaurantId) throws SQLException {
        return reviewDao.findByRestaurant(restaurantId);
    }

    public List<Review> findByUser(String userId) throws SQLException {
        return reviewDao.findByUser(userId);
    }
}
