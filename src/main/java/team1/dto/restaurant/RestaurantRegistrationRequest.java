package team1.dto.restaurant;

import java.util.List;

public class RestaurantRegistrationRequest {
    private RestaurantBasicDto restaurant;
    private List<RoomDto> rooms;
    private List<TimeslotRuleDto> timeslotRules;
    private List<BreakTimeDto> breakTimes;
    private List<SpecialDayDto> specialDays;
    private List<MenuDto> menus;

    public RestaurantBasicDto getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(RestaurantBasicDto restaurant) {
        this.restaurant = restaurant;
    }

    public List<RoomDto> getRooms() {
        return rooms;
    }

    public void setRooms(List<RoomDto> rooms) {
        this.rooms = rooms;
    }

    public List<TimeslotRuleDto> getTimeslotRules() {
        return timeslotRules;
    }

    public void setTimeslotRules(List<TimeslotRuleDto> timeslotRules) {
        this.timeslotRules = timeslotRules;
    }

    public List<BreakTimeDto> getBreakTimes() {
        return breakTimes;
    }

    public void setBreakTimes(List<BreakTimeDto> breakTimes) {
        this.breakTimes = breakTimes;
    }

    public List<SpecialDayDto> getSpecialDays() {
        return specialDays;
    }

    public void setSpecialDays(List<SpecialDayDto> specialDays) {
        this.specialDays = specialDays;
    }

    public List<MenuDto> getMenus() {
        return menus;
    }

    public void setMenus(List<MenuDto> menus) {
        this.menus = menus;
    }
}
