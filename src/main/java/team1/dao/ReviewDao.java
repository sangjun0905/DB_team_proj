package team1.dao;

import team1.config.DbUtil;
import team1.domain.review.Review;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewDao {

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

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, r.getId());
            ps.setString(2, r.getUserId());
            ps.setString(3, r.getBookingId());
            ps.setInt(4, r.getRating());
            ps.setString(5, r.getContent());
            ps.setString(6, r.getEmbeddingVector());

            ps.executeUpdate();
        }
    }

    // 리뷰 내용/별점 수정
    public void updateReview(String reviewId, int rating, String content) throws SQLException {
        String sql = """
            UPDATE review
            SET rating = ?,
                content = ?,
                updated_at = NOW()
            WHERE id = ? AND is_deleted = 0
            """;

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, rating);
            ps.setString(2, content);
            ps.setString(3, reviewId);

            ps.executeUpdate();
        }
    }

    // 리뷰 소프트 삭제
    public void softDeleteReview(String reviewId) throws SQLException {
        String sql = """
            UPDATE review
            SET is_deleted = 1,
                updated_at = NOW()
            WHERE id = ? AND is_deleted = 0
            """;

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, reviewId);
            ps.executeUpdate();
        }
    }

    // 매장별 리뷰 목록
    public List<Review> findByRestaurant(String restaurantId) throws SQLException {
        String sql = """
            SELECT rv.*
            FROM review rv
            JOIN booking b ON rv.booking_id = b.id
            WHERE b.restaurant_id = ?
              AND rv.is_deleted = 0
            ORDER BY rv.created_at DESC
            """;

        List<Review> list = new ArrayList<>();

        try (Connection conn = DbUtil.getConnection();
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

    // 유저별 리뷰 목록 (마이페이지 용)
    public List<Review> findByUser(String userId) throws SQLException {
        String sql = """
            SELECT *
            FROM review
            WHERE user_id = ?
              AND is_deleted = 0
            ORDER BY created_at DESC
            """;

        List<Review> list = new ArrayList<>();

        try (Connection conn = DbUtil.getConnection();
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

    public Review findById(String reviewId) throws SQLException {
        String sql = """
            SELECT *
            FROM review
            WHERE id = ? AND is_deleted = 0
            """;

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, reviewId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapReview(rs);
                }
            }
        }
        return null;
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
        Timestamp del = rs.getTimestamp("deleted_at");
        if (del != null) {
            r.setDeletedAt(del.toLocalDateTime());
        }
        return r;
    }
}
