package com.destimatch.common.utils;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Continent {
    AFRICA("Afrique"),
    ASIA("Asie"),
    MIDDLE_EAST("Moyen-Orient"),
    EUROPE("Europe"),
    NORTH_AMERICA("Amérique du Nord (USA/Canada)"),
    CENTRAL_AMERICA("Amérique Centrale & Caraïbes"),
    SOUTH_AMERICA("Amérique du Sud"),
    OCEANIA("Océanie"),
    ANTARCTICA("Antarctique");

    private final String label;

    Continent(String label) {
        this.label = label;
    }
}
