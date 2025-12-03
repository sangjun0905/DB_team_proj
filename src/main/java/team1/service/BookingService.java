package team1.service;

import team1.dao.BookingDao;
import team1.domain.booking.Booking;
import team1.dto.booking.CreateReservationRequest;
import team1.dto.booking.CreateWaitingRequest;
import team1.dto.booking.UpdateBookingStateRequest;

import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

/**
 * Thin service that wraps stored procedure calls for booking lifecycle.
 * Business rules are enforced in the database layer.
 */
public class BookingService {
    private final DataSource dataSource;
    private final BookingDao bookingDao;

    public BookingService(DataSource dataSource, BookingDao bookingDao) {
        this.dataSource = dataSource;
        this.bookingDao = bookingDao;
    }

    public String createReservation(CreateReservationRequest req) throws SQLException {
        String sql = "{ CALL sp_create_reservation(?,?,?,?,?,?) }";

        try (Connection conn = dataSource.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, req.getRestaurantId());
            cs.setString(2, req.getUserId());
            cs.setString(3, req.getTimeslotInstanceId());
            cs.setInt(4, req.getPersons());
            if (req.getCouponId() != null) {
                cs.setString(5, req.getCouponId());
            } else {
                cs.setNull(5, java.sql.Types.CHAR);
            }
            cs.registerOutParameter(6, java.sql.Types.CHAR);

            cs.execute();
            return cs.getString(6);
        }
    }

    public String createWaiting(CreateWaitingRequest req) throws SQLException {
        String sql = "{ CALL sp_create_waiting(?,?,?,?,?) }";

        try (Connection conn = dataSource.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, req.getRestaurantId());
            cs.setString(2, req.getUserId());
            cs.setInt(3, req.getPersons());
            cs.setTimestamp(4, Timestamp.valueOf(req.getWaitingStartTime()));
            cs.registerOutParameter(5, java.sql.Types.CHAR);

            cs.execute();
            return cs.getString(5);
        }
    }

    public void updateBookingState(UpdateBookingStateRequest req) throws SQLException {
        String sql = "{ CALL sp_update_booking_state(?,?,?,?) }";

        try (Connection conn = dataSource.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, req.getBookingId());
            cs.setString(2, req.getActorUserId());
            cs.setString(3, req.getNewState());
            cs.setString(4, req.getDescription());

            cs.execute();
        }
    }

    /**
     * Convenience helper: get bookings by restaurant using existing DAO.
     * Database rules are already enforced; this is for UI list retrieval.
     */
    public List<Booking> getBookingsByRestaurantAndDate(String restaurantId, java.time.LocalDate date) throws SQLException {
        return bookingDao.findByRestaurantAndDate(restaurantId, date);
    }
}
