package team1.dao.jdbc;

import team1.dao.RestaurantTimeslotRuleDao;
import team1.domain.timeslot.RestaurantTimeslotRule;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalTime;

public class JdbcRestaurantTimeslotRuleDao implements RestaurantTimeslotRuleDao {
    @Override
    public void insert(Connection conn, RestaurantTimeslotRule rule) throws SQLException {
        String sql = """
            INSERT INTO restaurant_timeslot_rule (
                id, restaurant_id, day_of_week, open_time, close_time,
                slot_interval, usage_time, team_capacity,
                is_holiday, allow_waiting, created_at, updated_at
            ) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)
            """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, rule.getId());
            ps.setString(2, rule.getRestaurantId());
            ps.setInt(3, rule.getDayOfWeek());
            ps.setTime(4, toSqlTime(rule.getOpenTime()));
            ps.setTime(5, toSqlTime(rule.getCloseTime()));
            ps.setInt(6, rule.getSlotInterval());
            ps.setInt(7, rule.getUsageTime());
            ps.setInt(8, rule.getTeamCapacity());
            ps.setBoolean(9, rule.isHoliday());
            ps.setBoolean(10, rule.isAllowWaiting());
            ps.setTimestamp(11, Timestamp.valueOf(rule.getCreatedAt()));
            ps.setTimestamp(12, Timestamp.valueOf(rule.getUpdatedAt()));
            ps.executeUpdate();
        }
    }

    private java.sql.Time toSqlTime(LocalTime t) {
        return t == null ? null : java.sql.Time.valueOf(t);
    }
}
