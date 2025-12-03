package team1.dto.restaurant;

import java.util.List;

public class MenuOptionGroupCreateRequest {
    private String name;
    private boolean required;
    private int maxSelect;
    private List<MenuOptionCreateRequest> options;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public int getMaxSelect() {
        return maxSelect;
    }

    public void setMaxSelect(int maxSelect) {
        this.maxSelect = maxSelect;
    }

    public List<MenuOptionCreateRequest> getOptions() {
        return options;
    }

    public void setOptions(List<MenuOptionCreateRequest> options) {
        this.options = options;
    }
}
