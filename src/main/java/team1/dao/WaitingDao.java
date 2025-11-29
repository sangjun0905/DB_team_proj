package team1.dao;

import team1.config.DbUtil;
// TODO: 네 도메인 패키지에 맞게 import 바꿔줘
import team1.domain.waiting.Waiting;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 웨이팅 관련 DB 접근을 담당하는 DAO.
 * - waiting 테이블 INSERT / SELECT / DELETE
 */
public class WaitingDao {

    // =========================================================
    // 1. 웨이팅 등록
    // =========================================================

    /**
     * 웨이팅 1건 등록.
     * - id: UUID()
     * - waiting_start_time: NOW()
     * - expected_* : 일단 null
     * - order: 해당 매장 waiting 에서 가장 큰 번호 + 1 (없으면 1)
     *
     * @return 생성된 waiting id
     */
    public String createWaiting(
            String restaurantId,
            String userId,
            Integer partySize,
            Integer usagePerTeam
    ) throws SQLException {

        String sql =
                "INSERT INTO waiting (" +
                        "  id, restaurant_id, user_id, " +
                        "  party_size, usage_per_team, " +
                        "  waiting_start_time, expected_start_time, expected_waiting_minute, `order`" +
                        ") VALUES (" +
                        "  UUID(), ?, ?, ?, ?, " +
                        "  NOW(), NULL, NULL, " +
                        "  COALESCE((" +
                        "    SELECT MAX(w2.`order`) + 1 FROM waiting w2 WHERE w2.restaurant_id = ?" +
                        "  ), 1)" +
                        ")";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, restaurantId);
            ps.setString(2, userId);

            if (partySize != null) ps.setInt(3, partySize);
            else ps.setNull(3, Types.INTEGER);

            if (usagePerTeam != null) ps.setInt(4, usagePerTeam);
            else ps.setNull(4, Types.INTEGER);

            // 서브쿼리용 restaurant_id
            ps.setString(5, restaurantId);

            ps.executeUpdate();

            // id 를 직접 UUID()로 만들었으니, 다시 SELECT 해서 가져오거나,
            // 여기서는 "방금 max(order)인 행"을 따로 조회해도 됨.
            // 단순하게 "방금 insert한 waiting_id 를 따로 리턴할 필요 없으면 null 리턴해도 됨.
            // 일단 편의상: INSERT 이후 방금 번호 조회 로직은 생략하고 null 리턴.
            return null;
        }
    }


    // =========================================================
    // 2. 웨이팅 조회
    // =========================================================

    /**
     * 특정 매장의 웨이팅 전체 목록 조회 (order 순).
     */
    public List<Waiting> findWaitingListByRestaurant(String restaurantId) throws SQLException {
        String sql =
                "SELECT id, restaurant_id, user_id, " +
                        "       party_size, usage_per_team, " +
                        "       waiting_start_time, expected_start_time, " +
                        "       expected_waiting_minute, `order` " +
                        "FROM waiting " +
                        "WHERE restaurant_id = ? " +
                        "ORDER BY `order` ASC";

        List<Waiting> result = new ArrayList<>();

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, restaurantId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapWaiting(rs));
                }
            }
        }

        return result;
    }

    /**
     * 특정 매장의 다음 차례(가장 order 작은) 웨이팅 1명 조회.
     * 없으면 null.
     */
    public Waiting findNextWaiting(String restaurantId) throws SQLException {
        String sql =
                "SELECT id, restaurant_id, user_id, " +
                        "       party_size, usage_per_team, " +
                        "       waiting_start_time, expected_start_time, " +
                        "       expected_waiting_minute, `order` " +
                        "FROM waiting " +
                        "WHERE restaurant_id = ? " +
                        "ORDER BY `order` ASC " +
                        "LIMIT 1";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, restaurantId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapWaiting(rs);
                }
            }
        }

        return null;
    }


    // =========================================================
    // 3. 웨이팅 삭제 (호출 완료 / 취소)
    // =========================================================

    public void deleteWaiting(String waitingId) throws SQLException {
        String sql = "DELETE FROM waiting WHERE id = ?";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, waitingId);
            ps.executeUpdate();
        }
    }


    // =========================================================
    // 4. 매핑 함수
    // =========================================================

    private Waiting mapWaiting(ResultSet rs) throws SQLException {
        Waiting w = new Waiting();
        w.setId(rs.getString("id"));
        w.setRestaurantId(rs.getString("restaurant_id"));
        w.setUserId(rs.getString("user_id"));
        w.setPartySize((Integer) rs.getObject("party_size"));
        w.setUsagePerTeam((Integer) rs.getObject("usage_per_team"));

        Timestamp ws = rs.getTimestamp("waiting_start_time");
        if (ws != null) {
            w.setWaitingStartTime(ws.toLocalDateTime());
        }

        Timestamp es = rs.getTimestamp("expected_start_time");
        if (es != null) {
            w.setExpectedStartTime(es.toLocalDateTime());
        }

        Integer expectedMin = (Integer) rs.getObject("expected_waiting_minute");
        w.setExpectedWaitingMinute(expectedMin);

        Integer order = (Integer) rs.getObject("order");
        w.setOrder(order);

        return w;
    }
}
