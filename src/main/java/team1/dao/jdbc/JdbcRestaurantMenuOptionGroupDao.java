package team1.dao.jdbc;

import team1.dao.RestaurantMenuOptionGroupDao;
import team1.domain.menu.RestaurantMenuOptionGroup;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class JdbcRestaurantMenuOptionGroupDao implements RestaurantMenuOptionGroupDao {
    @Override
    public void insert(Connection conn, RestaurantMenuOptionGroup group) throws SQLException {
        String sql = """
            INSERT INTO restaurant_menu_option_group (
                id, menu_id, name, is_required, max_select, created_at, updated_at
            ) VALUES (?,?,?,?,?,?,?)
            """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, group.getId());
            ps.setString(2, group.getMenuId());
            ps.setString(3, group.getName());
            ps.setBoolean(4, group.isRequired());
            ps.setInt(5, group.getMaxSelect());
            ps.setTimestamp(6, Timestamp.valueOf(group.getCreatedAt()));
            ps.setTimestamp(7, Timestamp.valueOf(group.getUpdatedAt()));
            ps.executeUpdate();
        }
    }
}
