package team1.dao;

import team1.config.DbUtil;
import team1.domain.reservation.Reservation;
import team1.domain.reservation.ReservationService;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 예약 관련 DB 접근을 담당하는 DAO.
 * - 예약 생성: sp_create_reservation 호출
 * - 예약 조회: reservation 테이블 SELECT
 * - 예약 서비스: reservation_service 테이블 INSERT / SELECT
 */
public class ReservationDao {

    // =========================================================
    // 1. 예약 생성 (sp_create_reservation)
    // =========================================================

    /**
     * 예약 1건 생성.
     * 내부에서 sp_create_reservation(...) 프로시저를 호출한다.
     *
     * 성공하면 그냥 끝나고,
     * 실패하면 SQLException이 던져진다.
     *   - SQLState == "45000" 이면 프로시저에서 SIGNAL 한 비즈니스 에러
     *     e.getMessage() 에 INVALID_USER, NO_ROOM_CAPACITY 같은 코드가 들어있음.
     */
    public void createReservation(
            String restaurantId,
            String userId,
            LocalDate date,
            LocalTime startTime,
            int persons
    ) throws SQLException {

        String sql = "{ call sp_create_reservation(?, ?, ?, ?, ?) }";

        try (Connection conn = DbUtil.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, restaurantId);
            cs.setString(2, userId);
            cs.setDate(3, Date.valueOf(date));
            cs.setTime(4, Time.valueOf(startTime));
            cs.setInt(5, persons);

            cs.execute();
        }
    }


    // =========================================================
    // 2. 예약 조회 (reservation 테이블)
    // =========================================================

    /**
     * reservation.id 기준 단건 조회. 없으면 null 리턴.
     */
    public Reservation findReservationById(String reservationId) throws SQLException {
        String sql =
                "SELECT id, restaurant_id, user_id, " +
                        "       cost, state, persons, " +
                        "       date, start_time, end_time, usage_time " +
                        "FROM reservation " +
                        "WHERE id = ?";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, reservationId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapReservation(rs);
                } else {
                    return null;
                }
            }
        }
    }

    /**
     * 특정 유저의 예약 목록 조회 (최신순).
     */
    public List<Reservation> findReservationsByUser(String userId) throws SQLException {
        String sql =
                "SELECT id, restaurant_id, user_id, " +
                        "       cost, state, persons, " +
                        "       date, start_time, end_time, usage_time " +
                        "FROM reservation " +
                        "WHERE user_id = ? " +
                        "ORDER BY date DESC, start_time DESC";

        List<Reservation> result = new ArrayList<>();

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapReservation(rs));
                }
            }
        }

        return result;
    }

    /**
     * 특정 매장 + 날짜 기준 예약 목록 조회 (시간순).
     * reservation.date 컬럼이 VARCHAR(255)라서 문자열 비교로 처리.
     */
    public List<Reservation> findReservationsByRestaurantAndDate(
            String restaurantId,
            LocalDate date
    ) throws SQLException {

        String sql =
                "SELECT id, restaurant_id, user_id, " +
                        "       cost, state, persons, " +
                        "       date, start_time, end_time, usage_time " +
                        "FROM reservation " +
                        "WHERE restaurant_id = ? " +
                        "  AND date = ? " +
                        "ORDER BY start_time";

        List<Reservation> result = new ArrayList<>();

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, restaurantId);
            ps.setString(2, date.toString()); // 'YYYY-MM-DD' 형태 문자열

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapReservation(rs));
                }
            }
        }

        return result;
    }

    /**
     * reservation ResultSet → Reservation 도메인 객체로 매핑.
     * Reservation 클래스의 필드/세터 이름에 맞게 필요하면 수정.
     */
    private Reservation mapReservation(ResultSet rs) throws SQLException {
        Reservation r = new Reservation();
        r.setId(rs.getString("id"));
        r.setRestaurantId(rs.getString("restaurant_id"));
        r.setUserId(rs.getString("user_id"));
        r.setCost(rs.getString("cost"));
        r.setState(rs.getString("state"));
        r.setPersons(rs.getString("persons"));
        r.setDate(rs.getString("date"));             // VARCHAR(255) → String
        r.setStartTime(rs.getString("start_time"));  // VARCHAR(255) → String
        r.setEndTime(rs.getString("end_time"));
        r.setUsageTime(rs.getString("usage_time"));
        return r;
    }


    // =========================================================
    // 3. 예약 서비스 (reservation_service 테이블)
    // =========================================================

    /**
     * 예약에 서비스 1건 추가.
     * - id 는 UUID()로 생성.
     */
    public void insertReservationService(
            String reservationId,
            String key,          // 컬럼명 `Key`
            String serviceId,
            String cost,
            String description,
            Integer serviceCnt
    ) throws SQLException {

        String sql =
                "INSERT INTO reservation_service " +
                        "  (id, reservation_id, `Key`, service_id, cost, description, service_cnt) " +
                        "VALUES " +
                        "  (UUID(), ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, reservationId);
            ps.setString(2, key);
            ps.setString(3, serviceId);
            ps.setString(4, cost);
            ps.setString(5, description);

            if (serviceCnt != null) {
                ps.setInt(6, serviceCnt);
            } else {
                ps.setNull(6, Types.INTEGER);
            }

            ps.executeUpdate();
        }
    }

    /**
     * 특정 예약에 연결된 reservation_service 목록 조회.
     */
    public List<ReservationService> findReservationServices(String reservationId) throws SQLException {
        String sql =
                "SELECT id, reservation_id, `Key`, service_id, " +
                        "       cost, description, service_cnt " +
                        "FROM reservation_service " +
                        "WHERE reservation_id = ?";

        List<ReservationService> result = new ArrayList<>();

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, reservationId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapReservationService(rs));
                }
            }
        }

        return result;
    }

    /**
     * reservation_service ResultSet → ReservationService 도메인 객체로 매핑.
     * ReservationService 클래스 구조에 맞게 세터 이름은 맞춰줘야 함.
     */
    private ReservationService mapReservationService(ResultSet rs) throws SQLException {
        ReservationService s = new ReservationService();
        s.setId(rs.getString("id"));
        s.setReservationId(rs.getString("reservation_id"));
        s.setKey(rs.getString("Key"));
        s.setServiceId(rs.getString("service_id"));
        s.setCost(rs.getString("cost"));
        s.setDescription(rs.getString("description"));
        int cnt = rs.getInt("service_cnt");
        if (rs.wasNull()) {
            s.setServiceCnt(null);
        } else {
            s.setServiceCnt(cnt);
        }
        return s;
    }

    // 필요하면 여기 아래에 타임슬롯 관련 SELECT / 프로시저 호출도 추가하면 됨.
}
