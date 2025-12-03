package team1.dao;

import org.springframework.stereotype.Repository;
import team1.domain.restaurant.Restaurant;
import team1.domain.restaurant.RestaurantRoom;
import team1.domain.timeslot.TimeslotInstance;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class RestaurantDao {

    private final DataSource dataSource;

    public RestaurantDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Restaurant findById(String id) throws SQLException {
        String sql = """
            SELECT *
            FROM restaurant
            WHERE id = ? AND is_deleted = 0
            """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRestaurant(rs);
                }
            }
        }
        return null;
    }

    public List<Restaurant> findAll() throws SQLException {
        String sql = """
            SELECT *
            FROM restaurant
            WHERE is_deleted = 0
            ORDER BY name
            """;
        List<Restaurant> list = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRestaurant(rs));
            }
        }
        return list;
    }

    public List<Restaurant> searchByNameOrKeyword(String keyword) throws SQLException {
        String sql = """
            SELECT DISTINCT r.*
            FROM restaurant r
            LEFT JOIN restaurant_keyword_map rkm ON r.id = rkm.restaurant_id
            LEFT JOIN restaurant_keyword rk ON rkm.keyword_id = rk.id
            WHERE r.is_deleted = 0
              AND (
                    r.name LIKE CONCAT('%', ?, '%')
                 OR rk.keyword LIKE CONCAT('%', ?, '%')
              )
            ORDER BY r.name
            """;

        List<Restaurant> list = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, keyword);
            ps.setString(2, keyword);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRestaurant(rs));
                }
            }
        }
        return list;
    }

    public List<RestaurantRoom> findRooms(String restaurantId) throws SQLException {
        String sql = """
            SELECT *
            FROM restaurant_room
            WHERE restaurant_id = ?
              AND is_deleted = 0
              AND is_active = 1
            ORDER BY capacity
            """;

        List<RestaurantRoom> list = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, restaurantId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    RestaurantRoom room = new RestaurantRoom();
                    room.setId(rs.getString("id"));
                    room.setRestaurantId(rs.getString("restaurant_id"));
                    room.setName(rs.getString("name"));
                    room.setCapacity(rs.getInt("capacity"));
                    room.setRoomType(rs.getString("room_type"));
                    room.setActive(rs.getBoolean("is_active"));
                    room.setDeleted(rs.getBoolean("is_deleted"));
                    room.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    room.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
                    Timestamp del = rs.getTimestamp("deleted_at");
                    if (del != null) {
                        room.setDeletedAt(del.toLocalDateTime());
                    }
                    list.add(room);
                }
            }
        }
        return list;
    }

    public List<TimeslotInstance> findTimeslots(String restaurantId,
                                                LocalDate date) throws SQLException {
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

    public void insert(Connection conn, Restaurant r) throws SQLException {
        String sql = """
            INSERT INTO restaurant (
                id, name, city_id, district_id, address, phone,
                supports_reservation, supports_waiting,
                avg_rating, review_count,
                is_deleted, created_at, updated_at
            ) VALUES (
                ?, ?, ?, ?, ?, ?,
                ?, ?,
                ?, ?,
                0, NOW(), NOW()
            )
            """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, r.getId());
            ps.setString(2, r.getName());
            ps.setString(3, r.getCityId());
            ps.setString(4, r.getDistrictId());
            ps.setString(5, r.getAddress());
            ps.setString(6, r.getPhone());
            ps.setBoolean(7, r.isSupportsReservation());
            ps.setBoolean(8, r.isSupportsWaiting());
            ps.setBigDecimal(9, r.getAvgRating());
            ps.setInt(10, r.getReviewCount());

            ps.executeUpdate();
        }
    }

    private Restaurant mapRestaurant(ResultSet rs) throws SQLException {
        Restaurant r = new Restaurant();
        r.setId(rs.getString("id"));
        r.setName(rs.getString("name"));
        r.setCityId(rs.getString("city_id"));
        r.setDistrictId(rs.getString("district_id"));
        r.setAddress(rs.getString("address"));
        r.setPhone(rs.getString("phone"));
        r.setSupportsReservation(rs.getBoolean("supports_reservation"));
        r.setSupportsWaiting(rs.getBoolean("supports_waiting"));
        r.setAvgRating(rs.getBigDecimal("avg_rating"));
        r.setReviewCount(rs.getInt("review_count"));
        r.setDeleted(rs.getBoolean("is_deleted"));
        r.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        r.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        Timestamp deletedAt = rs.getTimestamp("deleted_at");
        if (deletedAt != null) {
            r.setDeletedAt(deletedAt.toLocalDateTime());
        }
        return r;
    }
}
