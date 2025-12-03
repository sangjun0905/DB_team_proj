package team1.dao;

import org.springframework.stereotype.Repository;
import team1.domain.booking.Booking;
import team1.domain.booking.BookingRoom;
import team1.domain.booking.ReservationDetail;
import team1.domain.booking.WaitingDetail;
import team1.domain.common.BookingState;
import team1.domain.common.BookingType;

import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BookingDao {

    private final DataSource dataSource;

    public BookingDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * 프로시저 sp_create_reservation 호출.
     *
     * @return 생성된 booking_id
     */
    public String createReservation(String restaurantId,
                                    String userId,
                                    String timeslotInstanceId,
                                    int persons,
                                    String couponId) throws SQLException {

        String sql = "{ CALL sp_create_reservation(?,?,?,?,?,?) }";

        try (Connection conn = dataSource.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, restaurantId);
            cs.setString(2, userId);
            cs.setString(3, timeslotInstanceId);
            cs.setInt(4, persons);

            if (couponId != null) {
                cs.setString(5, couponId);
            } else {
                cs.setNull(5, java.sql.Types.CHAR);
            }

            cs.registerOutParameter(6, java.sql.Types.CHAR);

            cs.execute();

            return cs.getString(6); // OUT p_booking_id
        }
    }

    /**
     * 프로시저 sp_create_waiting 호출.
     *
     * @return 생성된 booking_id
     */
    public String createWaiting(String restaurantId,
                                String userId,
                                int persons,
                                LocalDateTime waitingStartTime) throws SQLException {

        String sql = "{ CALL sp_create_waiting(?,?,?,?,?) }";

        try (Connection conn = dataSource.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, restaurantId);
            cs.setString(2, userId);
            cs.setInt(3, persons);
            cs.setTimestamp(4, Timestamp.valueOf(waitingStartTime));
            cs.registerOutParameter(5, java.sql.Types.CHAR);

            cs.execute();

            return cs.getString(5);
        }
    }

    /**
     * 프로시저 sp_update_booking_state 호출.
     */
    public void updateBookingState(String bookingId,
                                   String actorUserId,
                                   String newState,
                                   String description) throws SQLException {

        String sql = "{ CALL sp_update_booking_state(?,?,?,?) }";

        try (Connection conn = dataSource.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, bookingId);
            cs.setString(2, actorUserId);
            cs.setString(3, newState);
            cs.setString(4, description);

            cs.execute();
        }
    }

    // ----------------------------------------------------------------
    // 기본 조회 메서드들 (UI에서 리스트 출력할 때 사용)
    // ----------------------------------------------------------------

    public Booking findById(String bookingId) throws SQLException {
        String sql = """
            SELECT *
            FROM booking
            WHERE id = ? AND is_deleted = 0
            """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, bookingId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapBooking(rs);
                }
                return null;
            }
        }
    }

    public List<Booking> findByUser(String userId) throws SQLException {
        String sql = """
            SELECT *
            FROM booking
            WHERE user_id = ? AND is_deleted = 0
            ORDER BY created_at DESC
            """;

        List<Booking> result = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapBooking(rs));
                }
            }
        }
        return result;
    }

    public List<Booking> findByRestaurantAndDate(String restaurantId,
                                                 LocalDate date) throws SQLException {
        String sql = """
            SELECT *
            FROM booking
            WHERE restaurant_id = ?
              AND date = ?
              AND is_deleted = 0
            ORDER BY start_time
            """;

        List<Booking> result = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, restaurantId);
            ps.setDate(2, Date.valueOf(date));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapBooking(rs));
                }
            }
        }

        return result;
    }

    // 예약 상세 / 웨이팅 상세 / 룸 배정 조회 --------------------------

    public ReservationDetail findReservationDetail(String bookingId) throws SQLException {
        String sql = """
            SELECT *
            FROM reservation_detail
            WHERE booking_id = ?
            """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, bookingId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ReservationDetail d = new ReservationDetail();
                    d.setBookingId(rs.getString("booking_id"));
                    d.setTimeslotInstanceId(rs.getString("timeslot_instance_id"));
                    d.setUsageMinutes(rs.getInt("usage_minutes"));
                    d.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    d.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
                    return d;
                }
            }
        }
        return null;
    }

    public WaitingDetail findWaitingDetail(String bookingId) throws SQLException {
        String sql = """
            SELECT *
            FROM waiting_detail
            WHERE booking_id = ?
            """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, bookingId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    WaitingDetail d = new WaitingDetail();
                    d.setBookingId(rs.getString("booking_id"));
                    d.setWaitingStartTime(rs.getTimestamp("waiting_start_time").toLocalDateTime());
                    Timestamp expectedStart = rs.getTimestamp("expected_start_time");
                    if (expectedStart != null) {
                        d.setExpectedStartTime(expectedStart.toLocalDateTime());
                    }
                    d.setExpectedWaitingMinute(rs.getInt("expected_waiting_minute"));
                    d.setQueueOrder(rs.getInt("queue_order"));
                    d.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    d.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
                    return d;
                }
            }
        }
        return null;
    }

    public List<BookingRoom> findBookingRooms(String bookingId) throws SQLException {
        String sql = """
            SELECT *
            FROM booking_room
            WHERE booking_id = ?
            """;

        List<BookingRoom> rooms = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, bookingId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    BookingRoom br = new BookingRoom();
                    br.setId(rs.getString("id"));
                    br.setBookingId(rs.getString("booking_id"));
                    br.setRoomId(rs.getString("room_id"));
                    br.setPersons(rs.getInt("persons"));
                    br.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    br.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
                    rooms.add(br);
                }
            }
        }
        return rooms;
    }

    // ----------------------------------------------------------------
    // 내부 매핑 함수
    // ----------------------------------------------------------------
    private Booking mapBooking(ResultSet rs) throws SQLException {
        Booking b = new Booking();
        b.setId(rs.getString("id"));
        b.setRestaurantId(rs.getString("restaurant_id"));
        b.setUserId(rs.getString("user_id"));
        String bookingType = rs.getString("booking_type");
        if (bookingType != null) {
            b.setBookingType(BookingType.valueOf(bookingType));
        }
        Date date = rs.getDate("date");
        if (date != null) {
            b.setDate(date.toLocalDate());
        }
        Time st = rs.getTime("start_time");
        if (st != null) {
            b.setStartTime(st.toLocalTime());
        }
        Time et = rs.getTime("end_time");
        if (et != null) {
            b.setEndTime(et.toLocalTime());
        }
        b.setPersons(rs.getInt("persons"));
        String state = rs.getString("state");
        if (state != null) {
            b.setState(BookingState.valueOf(state));
        }
        b.setCouponId(rs.getString("coupon_id"));
        b.setDiscountPrice(rs.getInt("discount_price"));
        b.setDeleted(rs.getBoolean("is_deleted"));
        b.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        b.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        Timestamp deletedAt = rs.getTimestamp("deleted_at");
        if (deletedAt != null) {
            b.setDeletedAt(deletedAt.toLocalDateTime());
        }
        return b;
    }
}
