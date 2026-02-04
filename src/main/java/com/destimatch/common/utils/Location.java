package com.destimatch.common.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonProperty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Location {

    // Indispensable pour calculer les distances
    private Double latitude;
    private Double longitude;

    private String city; // ex: "Paris"
    private String country; // ex: "France"

    @BsonProperty("country_code")
    private String countryCode; // ex: "FR"

    private Continent continent; // ex: EUROPE

}
