package team1.dao.jdbc;

import team1.dao.RestaurantSpecialDayDao;
import team1.domain.restaurant.RestaurantSpecialDay;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class JdbcRestaurantSpecialDayDao implements RestaurantSpecialDayDao {
    @Override
    public void insert(Connection conn, RestaurantSpecialDay sd) throws SQLException {
        String sql = """
            INSERT INTO restaurant_special_day (
                id, restaurant_id, date, type,
                allow_reservation, allow_waiting, memo,
                created_at, updated_at
            ) VALUES (?,?,?,?,?,?,?,?,?)
            """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, sd.getId());
            ps.setString(2, sd.getRestaurantId());
            ps.setDate(3, java.sql.Date.valueOf(sd.getDate()));
            ps.setString(4, sd.getType().name());
            ps.setBoolean(5, sd.isAllowReservation());
            ps.setBoolean(6, sd.isAllowWaiting());
            ps.setString(7, sd.getMemo());
            ps.setTimestamp(8, Timestamp.valueOf(sd.getCreatedAt()));
            ps.setTimestamp(9, Timestamp.valueOf(sd.getUpdatedAt()));
            ps.executeUpdate();
        }
    }
}
