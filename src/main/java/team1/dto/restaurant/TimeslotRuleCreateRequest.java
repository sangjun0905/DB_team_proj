package team1.dto.restaurant;

import java.time.LocalTime;

public class TimeslotRuleCreateRequest {
    private int dayOfWeek;
    private LocalTime openTime;
    private LocalTime closeTime;
    private int slotInterval;
    private int usageTime;
    private int teamCapacity;
    private boolean holiday;
    private boolean allowWaiting;

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public LocalTime getOpenTime() {
        return openTime;
    }

    public void setOpenTime(LocalTime openTime) {
        this.openTime = openTime;
    }

    public LocalTime getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(LocalTime closeTime) {
        this.closeTime = closeTime;
    }

    public int getSlotInterval() {
        return slotInterval;
    }

    public void setSlotInterval(int slotInterval) {
        this.slotInterval = slotInterval;
    }

    public int getUsageTime() {
        return usageTime;
    }

    public void setUsageTime(int usageTime) {
        this.usageTime = usageTime;
    }

    public int getTeamCapacity() {
        return teamCapacity;
    }

    public void setTeamCapacity(int teamCapacity) {
        this.teamCapacity = teamCapacity;
    }

    public boolean isHoliday() {
        return holiday;
    }

    public void setHoliday(boolean holiday) {
        this.holiday = holiday;
    }

    public boolean isAllowWaiting() {
        return allowWaiting;
    }

    public void setAllowWaiting(boolean allowWaiting) {
        this.allowWaiting = allowWaiting;
    }
}
