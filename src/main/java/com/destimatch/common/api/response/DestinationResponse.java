package com.destimatch.common.api.response;

import com.destimatch.common.utils.Location;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DestinationResponse {
    private String id;
    private String name;
    private String description;
    private List<String> images;
    private Location location;
    private List<String> tags;
    private Double averageDailyCost;
    private Double rating;
    private Integer reviewCount;
    private List<Integer> bestMonths;
}