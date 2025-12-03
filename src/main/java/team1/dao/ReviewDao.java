package team1.dao;

import org.springframework.stereotype.Repository;
import team1.domain.review.Review;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ReviewDao {

    private final DataSource dataSource;

    public ReviewDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // 리뷰 생성
    public void insertReview(Review r) throws SQLException {
        String sql = """
            INSERT INTO review (
                id, user_id, booking_id, rating, content,
                embedding_vector, is_deleted, created_at, updated_at
            ) VALUES (
                ?, ?, ?, ?, ?,
                ?, 0, NOW(), NOW()
            )
            """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, r.getId());
            ps.setString(2, r.getUserId());
            ps.setString(3, r.getBookingId());
            ps.setByte(4, r.getRating());
            ps.setString(5, r.getContent());
            ps.setString(6, r.getEmbeddingVector());

            ps.executeUpdate();
        }
    }

    // 리뷰 조회: 식당별
    public List<Review> findByRestaurant(String restaurantId) throws SQLException {
        String sql = """
            SELECT r.*
            FROM review r
            JOIN booking b ON r.booking_id = b.id
            WHERE b.restaurant_id = ?
              AND r.is_deleted = 0
              AND b.is_deleted = 0
            ORDER BY r.created_at DESC
            """;
        List<Review> list = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, restaurantId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapReview(rs));
                }
            }
        }
        return list;
    }

    // 리뷰 조회: 유저별
    public List<Review> findByUser(String userId) throws SQLException {
        String sql = """
            SELECT *
            FROM review
            WHERE user_id = ?
              AND is_deleted = 0
            ORDER BY created_at DESC
            """;
        List<Review> list = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapReview(rs));
                }
            }
        }
        return list;
    }

    private Review mapReview(ResultSet rs) throws SQLException {
        Review r = new Review();
        r.setId(rs.getString("id"));
        r.setUserId(rs.getString("user_id"));
        r.setBookingId(rs.getString("booking_id"));
        r.setRating(rs.getByte("rating"));
        r.setContent(rs.getString("content"));
        r.setEmbeddingVector(rs.getString("embedding_vector"));
        r.setDeleted(rs.getBoolean("is_deleted"));
        r.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        r.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        var del = rs.getTimestamp("deleted_at");
        if (del != null) {
            r.setDeletedAt(del.toLocalDateTime());
        }
        return r;
    }
}
