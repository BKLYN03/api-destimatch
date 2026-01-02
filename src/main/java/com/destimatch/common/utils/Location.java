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
    private String city;
    private String country;
    @BsonProperty("country_code")
    private String countryCode;
    private Continent continent;
}
