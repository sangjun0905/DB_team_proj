package team1.dao.jdbc;

import team1.dao.RestaurantUserRoleDao;
import team1.domain.restaurant.RestaurantUserRole;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class JdbcRestaurantUserRoleDao implements RestaurantUserRoleDao {
    @Override
    public void insert(Connection conn, RestaurantUserRole role) throws SQLException {
        String sql = """
            INSERT INTO restaurant_user_role (
                id, restaurant_id, user_id, role, created_at, updated_at
            ) VALUES (?,?,?,?,?,?)
            """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, role.getId());
            ps.setString(2, role.getRestaurantId());
            ps.setString(3, role.getUserId());
            ps.setString(4, role.getRole().name());
            ps.setTimestamp(5, Timestamp.valueOf(role.getCreatedAt()));
            ps.setTimestamp(6, Timestamp.valueOf(role.getUpdatedAt()));
            ps.executeUpdate();
        }
    }
}
