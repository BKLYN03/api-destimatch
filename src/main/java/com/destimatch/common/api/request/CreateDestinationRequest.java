package com.destimatch.common.api.request;

import com.destimatch.common.utils.Location;
import com.destimatch.common.utils.TravelStyle;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CreateDestinationRequest {
    private String name;
    private String description;
    private List<String> images;
    private Location location;

    private List<String> tags;
    private Double averageDailyCost;
    private List<Integer> bestMonths; // ex: [1, 2, 12] pour l'hiver

    private List<TravelStyle> compatibleStyles;
}
