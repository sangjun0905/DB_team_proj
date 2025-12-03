package team1.dao;

import team1.config.DbUtil;
import team1.domain.userrelation.Favorites;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FavoritesDao {

    // 즐겨찾기 추가 (이미 존재하면 무시하게 하려면 UNIQUE 제약 + IGNORE 사용도 가능)
    public void addFavorite(String userId, String restaurantId) throws SQLException {
        String sql = """
            INSERT INTO favorites (
                id, user_id, restaurant_id, created_at, updated_at
            ) VALUES (
                ?, ?, ?, NOW(), NOW()
            )
            """;

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, UUID.randomUUID().toString());
            ps.setString(2, userId);
            ps.setString(3, restaurantId);
            ps.executeUpdate();
        }
    }

    // 즐겨찾기 삭제
    public void deleteFavorite(String userId, String restaurantId) throws SQLException {
        String sql = """
            DELETE FROM favorites
            WHERE user_id = ? AND restaurant_id = ?
            """;

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, userId);
            ps.setString(2, restaurantId);
            ps.executeUpdate();
        }
    }

    // 존재 여부 체크 (UI에서 하트 표시 등)
    public boolean existsFavorite(String userId, String restaurantId) throws SQLException {
        String sql = """
            SELECT COUNT(*)
            FROM favorites
            WHERE user_id = ? AND restaurant_id = ?
            """;

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, userId);
            ps.setString(2, restaurantId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    // 유저별 즐겨찾기 목록
    public List<Favorites> findByUser(String userId) throws SQLException {
        String sql = """
            SELECT *
            FROM favorites
            WHERE user_id = ?
            ORDER BY created_at DESC
            """;

        List<Favorites> list = new ArrayList<>();

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Favorites f = new Favorites();
                    f.setId(rs.getString("id"));
                    f.setUserId(rs.getString("user_id"));
                    f.setRestaurantId(rs.getString("restaurant_id"));
                    f.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    f.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
                    list.add(f);
                }
            }
        }
        return list;
    }
}
