package com.destimatch.common.utils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Category {
    AMBIANCE,  // correspond à "AMBIANCE" dans Atlas
    METEO,     // correspond à "METEO"
    ACTIVITES, // correspond à "ACTIVITES"
    FINANCE,   // correspond à "FINANCE"
    SOCIAL,    // correspond à "SOCIAL"
    CONTEXTE   // correspond à "CONTEXTE"
}