package team1.dao.jdbc;

import team1.dao.RestaurantMenuDao;
import team1.domain.menu.RestaurantMenu;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class JdbcRestaurantMenuDao implements RestaurantMenuDao {
    @Override
    public void insert(Connection conn, RestaurantMenu menu) throws SQLException {
        String sql = """
            INSERT INTO restaurant_menu (
                id, restaurant_id, name, price, description, image_url,
                sort_order, is_active, is_deleted, created_at, updated_at
            ) VALUES (?,?,?,?,?,?,?,?,?,?,?)
            """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, menu.getId());
            ps.setString(2, menu.getRestaurantId());
            ps.setString(3, menu.getName());
            ps.setInt(4, menu.getPrice());
            ps.setString(5, menu.getDescription());
            ps.setString(6, menu.getImageUrl());
            ps.setInt(7, menu.getSortOrder());
            ps.setBoolean(8, menu.isActive());
            ps.setBoolean(9, menu.isDeleted());
            ps.setTimestamp(10, Timestamp.valueOf(menu.getCreatedAt()));
            ps.setTimestamp(11, Timestamp.valueOf(menu.getUpdatedAt()));
            ps.executeUpdate();
        }
    }
}
