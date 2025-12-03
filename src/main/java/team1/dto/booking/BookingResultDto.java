package team1.dto.booking;

public class BookingResultDto {
    private String bookingId;
    private String state;
    private String restaurantId;
    private String userId;

    public BookingResultDto() {
    }

    public BookingResultDto(String bookingId, String state, String restaurantId, String userId) {
        this.bookingId = bookingId;
        this.state = state;
        this.restaurantId = restaurantId;
        this.userId = userId;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
