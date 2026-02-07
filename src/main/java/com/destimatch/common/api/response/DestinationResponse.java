package com.destimatch.common.api.response;

import com.destimatch.common.utils.BudgetLevel;
import com.destimatch.common.utils.Location;
import com.destimatch.common.utils.TravelStyle;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.Map;

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
    private Map<String, Double> aiTags;
    private String aiSummary;
    private Double averageDailyCost;
    private String budgetLevel;
    private Double rating;
    private Integer reviewCount;
    private List<Integer> bestMonths;
    private List<TravelStyle> compatibleStyles;
}