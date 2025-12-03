package team1.dto.booking;

import java.time.LocalDateTime;

public class CreateWaitingRequest {
    private String restaurantId;
    private String userId;
    private int persons;
    private LocalDateTime waitingStartTime;

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

    public int getPersons() {
        return persons;
    }

    public void setPersons(int persons) {
        this.persons = persons;
    }

    public LocalDateTime getWaitingStartTime() {
        return waitingStartTime;
    }

    public void setWaitingStartTime(LocalDateTime waitingStartTime) {
        this.waitingStartTime = waitingStartTime;
    }
}
