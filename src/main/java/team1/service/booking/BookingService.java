package team1.service.booking;

import team1.dto.booking.BookingResultDto;
import team1.dto.booking.CreateReservationRequestDto;
import team1.dto.booking.CreateWaitingRequestDto;
import team1.dto.booking.UpdateBookingStateRequestDto;

import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;

public class BookingService {

    private final DataSource dataSource;

    public BookingService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public BookingResultDto createReservation(CreateReservationRequestDto request)
            throws SQLException, ReservationBusinessException {
        String sql = "{ CALL sp_create_reservation(?,?,?,?,?,?) }";
        try (Connection conn = dataSource.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, request.getRestaurantId());
            cs.setString(2, request.getUserId());
            cs.setString(3, request.getTimeslotInstanceId());
            cs.setInt(4, request.getPersons());
            if (request.getCouponId() != null) {
                cs.setString(5, request.getCouponId());
            } else {
                cs.setNull(5, java.sql.Types.CHAR);
            }
            cs.registerOutParameter(6, java.sql.Types.CHAR);

            executeWithBusinessHandling(cs);

            String bookingId = cs.getString(6);
            return new BookingResultDto(bookingId, "RESERVED",
                    request.getRestaurantId(), request.getUserId());
        }
    }

    public BookingResultDto createWaiting(CreateWaitingRequestDto request)
            throws SQLException, ReservationBusinessException {
        String sql = "{ CALL sp_create_waiting(?,?,?,?,?) }";
        try (Connection conn = dataSource.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, request.getRestaurantId());
            cs.setString(2, request.getUserId());
            cs.setInt(3, request.getPersons());
            cs.setTimestamp(4, Timestamp.valueOf(request.getWaitingStartTime()));
            cs.registerOutParameter(5, java.sql.Types.CHAR);

            executeWithBusinessHandling(cs);

            String bookingId = cs.getString(5);
            return new BookingResultDto(bookingId, "WAITING",
                    request.getRestaurantId(), request.getUserId());
        }
    }

    public void updateBookingState(UpdateBookingStateRequestDto request)
            throws SQLException, ReservationBusinessException {
        String sql = "{ CALL sp_update_booking_state(?,?,?,?) }";
        try (Connection conn = dataSource.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, request.getBookingId());
            cs.setString(2, request.getActorUserId());
            cs.setString(3, request.getNewState());
            cs.setString(4, request.getDescription());

            executeWithBusinessHandling(cs);
        }
    }

    private void executeWithBusinessHandling(CallableStatement cs) throws SQLException, ReservationBusinessException {
        try {
            cs.execute();
        } catch (SQLException e) {
            if ("45000".equals(e.getSQLState())) {
                throw new ReservationBusinessException(e.getMessage(), e);
            }
            throw e;
        }
    }
}
