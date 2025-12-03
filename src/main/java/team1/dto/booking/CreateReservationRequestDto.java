package team1.dto.booking;

public class CreateReservationRequestDto {
    private String restaurantId;
    private String userId;
    private String timeslotInstanceId;
    private int persons;
    private String couponId;

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

    public String getTimeslotInstanceId() {
        return timeslotInstanceId;
    }

    public void setTimeslotInstanceId(String timeslotInstanceId) {
        this.timeslotInstanceId = timeslotInstanceId;
    }

    public int getPersons() {
        return persons;
    }

    public void setPersons(int persons) {
        this.persons = persons;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }
}
