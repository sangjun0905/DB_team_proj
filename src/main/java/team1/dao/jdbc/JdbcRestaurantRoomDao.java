package team1.dao.jdbc;

import team1.dao.RestaurantRoomDao;
import team1.domain.restaurant.RestaurantRoom;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class JdbcRestaurantRoomDao implements RestaurantRoomDao {
    @Override
    public void insert(Connection conn, RestaurantRoom room) throws SQLException {
        String sql = """
            INSERT INTO restaurant_room (
                id, restaurant_id, name, capacity, room_type,
                is_active, description, is_deleted, created_at, updated_at
            ) VALUES (?,?,?,?,?,?,?,?,?,?)
            """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, room.getId());
            ps.setString(2, room.getRestaurantId());
            ps.setString(3, room.getName());
            ps.setInt(4, room.getCapacity());
            ps.setString(5, room.getRoomType());
            ps.setBoolean(6, room.isActive());
            ps.setString(7, room.getDescription());
            ps.setBoolean(8, room.isDeleted());
            ps.setTimestamp(9, Timestamp.valueOf(room.getCreatedAt()));
            ps.setTimestamp(10, Timestamp.valueOf(room.getUpdatedAt()));
            ps.executeUpdate();
        }
    }
}
