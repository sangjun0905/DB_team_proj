package team1.dao.jdbc;

import org.springframework.stereotype.Repository;
import team1.dao.RestaurantBreakTimeDao;
import team1.domain.restaurant.RestaurantBreakTime;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalTime;

@Repository
public class JdbcRestaurantBreakTimeDao implements RestaurantBreakTimeDao {
    @Override
    public void insert(Connection conn, RestaurantBreakTime bt) throws SQLException {
        String sql = """
            INSERT INTO restaurant_break_time (
                id, restaurant_id, day_of_week, break_start_time, break_end_time,
                apply_date, is_active, memo, created_at, updated_at
            ) VALUES (?,?,?,?,?,?,?,?,?,?)
            """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, bt.getId());
            ps.setString(2, bt.getRestaurantId());
            ps.setInt(3, bt.getDayOfWeek());
            ps.setTime(4, toSqlTime(bt.getBreakStartTime()));
            ps.setTime(5, toSqlTime(bt.getBreakEndTime()));
            if (bt.getApplyDate() != null) {
                ps.setDate(6, java.sql.Date.valueOf(bt.getApplyDate()));
            } else {
                ps.setNull(6, java.sql.Types.DATE);
            }
            ps.setBoolean(7, bt.isActive());
            ps.setString(8, bt.getMemo());
            ps.setTimestamp(9, Timestamp.valueOf(bt.getCreatedAt()));
            ps.setTimestamp(10, Timestamp.valueOf(bt.getUpdatedAt()));
            ps.executeUpdate();
        }
    }

    private java.sql.Time toSqlTime(LocalTime t) {
        return t == null ? null : java.sql.Time.valueOf(t);
    }
}
