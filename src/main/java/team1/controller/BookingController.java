package team1.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team1.dto.booking.BookingResultDto;
import team1.dto.booking.CreateReservationRequestDto;
import team1.dto.booking.CreateWaitingRequestDto;
import team1.dto.booking.UpdateBookingStateRequestDto;
import team1.service.booking.BookingService;
import team1.service.booking.ReservationBusinessException;

import java.sql.SQLException;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/reservation")
    public ResponseEntity<BookingResultDto> createReservation(@RequestBody CreateReservationRequestDto req)
            throws SQLException, ReservationBusinessException {
        return ResponseEntity.ok(bookingService.createReservation(req));
    }

    @PostMapping("/waiting")
    public ResponseEntity<BookingResultDto> createWaiting(@RequestBody CreateWaitingRequestDto req)
            throws SQLException, ReservationBusinessException {
        return ResponseEntity.ok(bookingService.createWaiting(req));
    }

    @PostMapping("/state")
    public ResponseEntity<Void> updateBookingState(@RequestBody UpdateBookingStateRequestDto req)
            throws SQLException, ReservationBusinessException {
        bookingService.updateBookingState(req);
        return ResponseEntity.ok().build();
    }
}
