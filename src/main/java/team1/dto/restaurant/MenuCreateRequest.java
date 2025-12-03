package team1.dto.restaurant;

import java.util.List;

public class MenuCreateRequest {
    private String name;
    private int price;
    private String description;
    private String imageUrl;
    private int sortOrder;
    private boolean active;
    private List<MenuOptionGroupCreateRequest> optionGroups;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<MenuOptionGroupCreateRequest> getOptionGroups() {
        return optionGroups;
    }

    public void setOptionGroups(List<MenuOptionGroupCreateRequest> optionGroups) {
        this.optionGroups = optionGroups;
    }
}
