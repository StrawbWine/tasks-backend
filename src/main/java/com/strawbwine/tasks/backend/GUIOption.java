package com.strawbwine.tasks.backend;

public class GUIOption {
    private int id;
    private OptionType optionType;
    private String description;

    public GUIOption(int id, OptionType optionType, String description) {
        this.id = id;
        this.optionType = optionType;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public OptionType getOptionType() {
        return optionType;
    }

    public void setOptionType(OptionType optionType) {
        this.optionType = optionType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return String.format("%d: %s", id, description);
    }
}
