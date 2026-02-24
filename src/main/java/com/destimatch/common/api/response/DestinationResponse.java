package com.destimatch.common.api.response;

import com.destimatch.common.utils.Location;
import com.destimatch.common.utils.TravelStyle;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DestinationResponse {
    private String id;
    private String name;
    private String description;
    private List<String> images;
    private Location location;
    private List<String> officialTags;
    private Double aiScoreCleanliness;
    private Double aiScoreVibe;
    private Double aiScorePrice;
    private Set<String> communityTags;
    private Double averageDailyCost;
    private String budgetLevel;
    private Double rating;
    private Integer reviewCount;
    private List<Integer> bestMonths;
    private List<TravelStyle> compatibleStyles;
}