package team1.dto.restaurant;

import java.time.LocalDate;

public class SpecialDayCreateRequest {
    private LocalDate date;
    private String type; // HOLIDAY, BLACKOUT, SPECIAL_OPEN
    private boolean allowReservation;
    private boolean allowWaiting;
    private String memo;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isAllowReservation() {
        return allowReservation;
    }

    public void setAllowReservation(boolean allowReservation) {
        this.allowReservation = allowReservation;
    }

    public boolean isAllowWaiting() {
        return allowWaiting;
    }

    public void setAllowWaiting(boolean allowWaiting) {
        this.allowWaiting = allowWaiting;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
