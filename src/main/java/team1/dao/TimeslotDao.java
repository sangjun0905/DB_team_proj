package team1.dao;

import org.springframework.stereotype.Repository;
import team1.domain.timeslot.RestaurantTimeslotRule;
import team1.domain.timeslot.TimeslotInstance;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TimeslotDao {

    private final DataSource dataSource;

    public TimeslotDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<RestaurantTimeslotRule> findRulesByRestaurant(String restaurantId) throws SQLException {
        String sql = """
            SELECT *
            FROM restaurant_timeslot_rule
            WHERE restaurant_id = ?
            """;
        List<RestaurantTimeslotRule> list = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, restaurantId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    RestaurantTimeslotRule r = new RestaurantTimeslotRule();
                    r.setId(rs.getString("id"));
                    r.setRestaurantId(rs.getString("restaurant_id"));
                    r.setDayOfWeek(rs.getInt("day_of_week"));
                    r.setOpenTime(rs.getTime("open_time").toLocalTime());
                    r.setCloseTime(rs.getTime("close_time").toLocalTime());
                    r.setSlotInterval(rs.getInt("slot_interval"));
                    r.setUsageTime(rs.getInt("usage_time"));
                    r.setTeamCapacity(rs.getInt("team_capacity"));
                    r.setHoliday(rs.getBoolean("is_holiday"));
                    r.setAllowWaiting(rs.getBoolean("allow_waiting"));
                    r.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    r.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
                    list.add(r);
                }
            }
        }
        return list;
    }

    public int insertTimeslotInstance(TimeslotInstance ts) throws SQLException {
        String sql = """
            INSERT INTO timeslot_instance (
                id, rule_id, date, start_time,
                team_capacity, reserved_team, is_active,
                created_at, updated_at
            ) VALUES (?,?,?,?,?,?,?,NOW(),NOW())
            """;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ts.getId());
            ps.setString(2, ts.getRuleId());
            ps.setDate(3, Date.valueOf(ts.getDate()));
            ps.setTime(4, Time.valueOf(ts.getStartTime()));
            ps.setInt(5, ts.getTeamCapacity());
            ps.setInt(6, ts.getReservedTeam());
            ps.setBoolean(7, ts.isActive());
            return ps.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException dup) {
            // 중복이면 0으로 간주
            return 0;
        }
    }

    public List<TimeslotInstance> findInstances(String restaurantId, LocalDate date) throws SQLException {
        String sql = """
            SELECT ti.*
            FROM timeslot_instance ti
            JOIN restaurant_timeslot_rule rtr ON ti.rule_id = rtr.id
            WHERE rtr.restaurant_id = ?
              AND ti.date = ?
            ORDER BY ti.start_time
            """;
        List<TimeslotInstance> list = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, restaurantId);
            ps.setDate(2, Date.valueOf(date));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    TimeslotInstance t = new TimeslotInstance();
                    t.setId(rs.getString("id"));
                    t.setRuleId(rs.getString("rule_id"));
                    t.setDate(rs.getDate("date").toLocalDate());
                    t.setStartTime(rs.getTime("start_time").toLocalTime());
                    t.setTeamCapacity(rs.getInt("team_capacity"));
                    t.setReservedTeam(rs.getInt("reserved_team"));
                    t.setActive(rs.getBoolean("is_active"));
                    t.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    t.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
                    list.add(t);
                }
            }
        }
        return list;
    }
}
