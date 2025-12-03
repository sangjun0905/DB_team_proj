package team1.facade;

import team1.dto.booking.BookingResultDto;
import team1.dto.booking.CreateReservationRequestDto;
import team1.dto.booking.CreateWaitingRequestDto;
import team1.dto.booking.UpdateBookingStateRequestDto;
import team1.dto.restaurant.RestaurantRegistrationRequest;
import team1.service.booking.BookingService;
import team1.service.restaurant.RestaurantRegistrationService;

import java.util.logging.Logger;

public class BookingManagementFacade {
    private static final Logger log = Logger.getLogger(BookingManagementFacade.class.getName());

    private final BookingService bookingService;
    private final RestaurantRegistrationService restaurantRegistrationService;

    public BookingManagementFacade(BookingService bookingService,
                                   RestaurantRegistrationService restaurantRegistrationService) {
        this.bookingService = bookingService;
        this.restaurantRegistrationService = restaurantRegistrationService;
    }

    public BookingResultDto createReservationForUser(String userId, CreateReservationRequestDto req) throws Exception {
        // TODO: 권한 체크 (userId == req.userId)
        req.setUserId(userId);
        log.fine("createReservationForUser user=" + userId);
        return bookingService.createReservation(req);
    }

    public BookingResultDto createWaitingForUser(String userId, CreateWaitingRequestDto req) throws Exception {
        // TODO: 권한 체크 (userId == req.userId)
        req.setUserId(userId);
        log.fine("createWaitingForUser user=" + userId);
        return bookingService.createWaiting(req);
    }

    public void changeBookingStateByOwner(String ownerUserId, UpdateBookingStateRequestDto req) throws Exception {
        // TODO: 권한 체크 (ownerUserId 가 해당 식당 OWNER/MANAGER 인지)
        req.setActorUserId(ownerUserId);
        log.fine("changeBookingStateByOwner actor=" + ownerUserId + " booking=" + req.getBookingId());
        bookingService.updateBookingState(req);
    }

    public String registerRestaurantForOwner(RestaurantRegistrationRequest request) throws Exception {
        // TODO: 권한 체크 (ownerUserId == request.restaurant.ownerUserId)
        log.fine("registerRestaurantForOwner owner=" + request.getRestaurant().getOwnerUserId());
        return restaurantRegistrationService.registerRestaurant(request);
    }
}
