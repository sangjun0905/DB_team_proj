package team1.dao.jdbc;

import team1.dao.RestaurantMenuOptionDao;
import team1.domain.menu.RestaurantMenuOption;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class JdbcRestaurantMenuOptionDao implements RestaurantMenuOptionDao {
    @Override
    public void insert(Connection conn, RestaurantMenuOption option) throws SQLException {
        String sql = """
            INSERT INTO restaurant_menu_option (
                id, option_group_id, name, price, created_at, updated_at
            ) VALUES (?,?,?,?,?,?)
            """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, option.getId());
            ps.setString(2, option.getOptionGroupId());
            ps.setString(3, option.getName());
            ps.setInt(4, option.getPrice());
            ps.setTimestamp(5, Timestamp.valueOf(option.getCreatedAt()));
            ps.setTimestamp(6, Timestamp.valueOf(option.getUpdatedAt()));
            ps.executeUpdate();
        }
    }
}
