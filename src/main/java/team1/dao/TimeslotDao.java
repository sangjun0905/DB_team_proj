package team1.dao;

import team1.config.DbUtil;
// TODO: 실제 도메인 클래스 패키지에 맞게 import 수정
import team1.domain.restaurant.RestaurantTimeslotRule;
import team1.domain.timeslot.TimeslotInstance;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 타임슬롯/영업시간 관련 DAO.
 * - restaurant_timeslot_rule 조회
 * - timeslot_instance 조회
 * - sp_generate_timeslots_for_date / _for_range 호출
 */
public class TimeslotDao {

    // =========================================================
    // 1. 타임슬롯 생성 (프로시저 호출)
    // =========================================================

    /**
     * 특정 매장의 특정 날짜에 대해 타임슬롯 인스턴스를 생성/갱신.
     * 내부에서 sp_generate_timeslots_for_date 호출.
     */
    public void generateTimeslotsForDate(String restaurantId, LocalDate date) throws SQLException {
        String sql = "{ call sp_generate_timeslots_for_date(?, ?) }";

        try (Connection conn = DbUtil.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, restaurantId);
            cs.setDate(2, Date.valueOf(date));

            cs.execute();
        }
    }

    /**
     * 특정 매장의 날짜 범위에 대해 타임슬롯 인스턴스를 생성/갱신.
     * sp_generate_timeslots_for_range 호출.
     */
    public void generateTimeslotsForRange(
            String restaurantId,
            LocalDate fromDate,
            LocalDate toDate
    ) throws SQLException {

        String sql = "{ call sp_generate_timeslots_for_range(?, ?, ?) }";

        try (Connection conn = DbUtil.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, restaurantId);
            cs.setDate(2, Date.valueOf(fromDate));
            cs.setDate(3, Date.valueOf(toDate));

            cs.execute();
        }
    }


    // =========================================================
    // 2. 타임슬롯 인스턴스 조회 (timeslot_instance)
    // =========================================================

    /**
     * 특정 매장 + 날짜 기준 타임슬롯 목록 조회.
     * - restaurant_timeslot_rule 과 조인해서 restaurant_id 조건을 건다.
     */
    public List<TimeslotInstance> findTimeslotsByRestaurantAndDate(
            String restaurantId,
            LocalDate date
    ) throws SQLException {

        String sql =
                "SELECT ti.id, ti.timeslot_rule_id, ti.date, ti.start_time, " +
                        "       ti.team_capacity, ti.reserved_team, ti.is_active " +
                        "FROM timeslot_instance ti " +
                        "JOIN restaurant_timeslot_rule rtr ON ti.timeslot_rule_id = rtr.id " +
                        "WHERE rtr.restaurant_id = ? " +
                        "  AND ti.date = ? " +
                        "ORDER BY ti.start_time";

        List<TimeslotInstance> result = new ArrayList<>();

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, restaurantId);
            ps.setDate(2, Date.valueOf(date));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapTimeslotInstance(rs));
                }
            }
        }

        return result;
    }

    private TimeslotInstance mapTimeslotInstance(ResultSet rs) throws SQLException {
        TimeslotInstance t = new TimeslotInstance();
        t.setId(rs.getString("id"));
        t.setTimeslotRuleId(rs.getString("timeslot_rule_id"));
        t.setDate(rs.getDate("date").toLocalDate());
        t.setStartTime(rs.getTime("start_time").toLocalTime());
        t.setTeamCapacity(rs.getInt("team_capacity"));
        t.setReservedTeam(rs.getInt("reserved_team"));
        t.setActive(rs.getBoolean("is_active"));
        return t;
    }


    // =========================================================
    // 3. 타임슬롯 룰 조회 (restaurant_timeslot_rule)
    // =========================================================

    /**
     * 특정 매장의 요일별 타임슬롯 룰 목록 조회.
     * - 영업시간 설정 화면 등에서 사용 가능.
     */
    public List<RestaurantTimeslotRule> findTimeslotRulesByRestaurant(String restaurantId) throws SQLException {
        String sql =
                "SELECT id, day_of_week, open_time, close_time, " +
                        "       slot_interval, usage_time, team_capacity, " +
                        "       allow_waiting, restaurant_id, is_holiday " +
                        "FROM restaurant_timeslot_rule " +
                        "WHERE restaurant_id = ? " +
                        "ORDER BY day_of_week, open_time";

        List<RestaurantTimeslotRule> result = new ArrayList<>();

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, restaurantId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapTimeslotRule(rs));
                }
            }
        }

        return result;
    }

    private RestaurantTimeslotRule mapTimeslotRule(ResultSet rs) throws SQLException {
        RestaurantTimeslotRule rule = new RestaurantTimeslotRule();
        rule.setId(rs.getString("id"));
        rule.setDayOfWeek(rs.getInt("day_of_week"));
        rule.setOpenTime(rs.getTime("open_time"));   // 도메인에서 LocalTime 쓰면 적절히 변환
        rule.setCloseTime(rs.getTime("close_time"));
        rule.setSlotInterval(rs.getInt("slot_interval"));
        rule.setUsageTime(rs.getInt("usage_time"));
        rule.setTeamCapacity(rs.getInt("team_capacity"));
        rule.setAllowWaiting(rs.getBoolean("allow_waiting"));
        rule.setRestaurantId(rs.getString("restaurant_id"));
        rule.setHoliday(rs.getBoolean("is_holiday"));
        return rule;
    }
}
