package team1.dto.restaurant;

public class RestaurantBasicDto {
    private String ownerUserId;
    private String name;
    private String cityId;
    private String districtId;
    private String address;
    private String phone;
    private boolean supportsReservation;
    private boolean supportsWaiting;

    public String getOwnerUserId() {
        return ownerUserId;
    }

    public void setOwnerUserId(String ownerUserId) {
        this.ownerUserId = ownerUserId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isSupportsReservation() {
        return supportsReservation;
    }

    public void setSupportsReservation(boolean supportsReservation) {
        this.supportsReservation = supportsReservation;
    }

    public boolean isSupportsWaiting() {
        return supportsWaiting;
    }

    public void setSupportsWaiting(boolean supportsWaiting) {
        this.supportsWaiting = supportsWaiting;
    }
}
