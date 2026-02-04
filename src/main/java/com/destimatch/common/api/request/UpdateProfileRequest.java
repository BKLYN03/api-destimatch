package com.destimatch.common.api.request;

import com.destimatch.common.utils.BudgetLevel;
import com.destimatch.common.utils.TravelStyle;
import com.destimatch.common.utils.Location;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
public class UpdateProfileRequest {
    private String name;
    private String phone;
    private Location location;
    private TravelStyle travelStyle;
    private BudgetLevel budgetLevel;
    private List<String> preferences;
}
