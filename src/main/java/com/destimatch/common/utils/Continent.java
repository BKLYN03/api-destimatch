package com.destimatch.common.utils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Continent {
    AFRICA("Afrique"),
    ASIA("Asie"),
    MIDDLE_EAST("Moyen-Orient"),
    EUROPE("Europe"),
    NORTH_AMERICA("Amérique du Nord"),
    CENTRAL_AMERICA("Amérique Centrale & Caraïbes"),
    SOUTH_AMERICA("Amérique du Sud"),
    OCEANIA("Océanie"),
    ANTARCTICA("Antarctique");

    private final String label;

    Continent(String label) {
        this.label = label;
    }

    @JsonValue
    public String getLabel() {
        return label;
    }

    @JsonCreator
    public static Continent fromString(String value) {
        if (value == null) return null;

        for (Continent c : Continent.values()) {
            if (c.name().equalsIgnoreCase(value) || c.label.equalsIgnoreCase(value)) {
                return c;
            }
        }
        return null;
    }
}
