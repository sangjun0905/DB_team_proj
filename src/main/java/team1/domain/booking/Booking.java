package team1.domain.booking;

import lombok.Data;
import team1.domain.common.BookingState;
import team1.domain.common.BookingType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class Booking {
    private String id;
    private String restaurantId;
    private String userId;
    private BookingType bookingType;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private int persons;
    private BookingState state;
    private String couponId;
    private int discountPrice;
    private boolean isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}
