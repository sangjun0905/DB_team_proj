package team1.service;

import team1.dto.booking.UpdateBookingStateRequest;

/**
 * High level façade for booking-related use cases with role awareness.
 * Permission checks are left as TODOs.
 */
public class BookingManagementFacade {

    private final BookingService bookingService;

    public BookingManagementFacade(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // For NORMAL users: cancel own reservation/waiting
    public void cancelMyBooking(String userId, String bookingId) throws Exception {
        // TODO: Check booking belongs to userId (via DAO)
        UpdateBookingStateRequest req = new UpdateBookingStateRequest();
        req.setBookingId(bookingId);
        req.setActorUserId(userId);
        req.setNewState("CANCELED");
        req.setDescription("User canceled");
        bookingService.updateBookingState(req);
    }

    // For restaurant OWNER/MANAGER: confirm reservation
    public void confirmReservation(String actorUserId, String bookingId) throws Exception {
        // TODO: Check actorUserId has OWNER/MANAGER role for the restaurant
        UpdateBookingStateRequest req = new UpdateBookingStateRequest();
        req.setBookingId(bookingId);
        req.setActorUserId(actorUserId);
        req.setNewState("CONFIRMED");
        req.setDescription("Reservation confirmed by staff");
        bookingService.updateBookingState(req);
    }

    public void seatReservation(String actorUserId, String bookingId) throws Exception {
        // TODO: permission check
        UpdateBookingStateRequest req = new UpdateBookingStateRequest();
        req.setBookingId(bookingId);
        req.setActorUserId(actorUserId);
        req.setNewState("SEATED");
        req.setDescription("Seated");
        bookingService.updateBookingState(req);
    }

    public void markNoshow(String actorUserId, String bookingId) throws Exception {
        // TODO: permission check
        UpdateBookingStateRequest req = new UpdateBookingStateRequest();
        req.setBookingId(bookingId);
        req.setActorUserId(actorUserId);
        req.setNewState("NOSHOW");
        req.setDescription("No-show marked by staff");
        bookingService.updateBookingState(req);
    }

    public void callWaiting(String actorUserId, String bookingId) throws Exception {
        // TODO: permission check
        UpdateBookingStateRequest req = new UpdateBookingStateRequest();
        req.setBookingId(bookingId);
        req.setActorUserId(actorUserId);
        req.setNewState("CALLED");
        req.setDescription("Waiting called");
        bookingService.updateBookingState(req);
    }

    public void seatWaiting(String actorUserId, String bookingId) throws Exception {
        // TODO: permission check
        UpdateBookingStateRequest req = new UpdateBookingStateRequest();
        req.setBookingId(bookingId);
        req.setActorUserId(actorUserId);
        req.setNewState("SEATED");
        req.setDescription("Waiting seated");
        bookingService.updateBookingState(req);
    }
}
