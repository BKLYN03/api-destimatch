package com.destimatch.common.utils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Category {
    ACTIVITES("Activités"),
    AMBIANCE("Ambiance"),
    METEO("Météo"),
    FINANCE("Finance"),
    BUDGET("Budget"),
    CONTEXTE("Contexte"),
    SOCIAL("Social");

    private final String label;

    Category(String label) {
        this.label = label;
    }

    @JsonValue
    public String getLabel() {
        return label;
    }

    @JsonCreator
    public static Category fromString(String value) {
        for (Category c : Category.values()) {
            if (c.label.equalsIgnoreCase(value))
                return c;
        }

        return null;
    }
}