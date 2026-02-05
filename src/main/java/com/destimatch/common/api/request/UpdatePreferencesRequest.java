package com.destimatch.common.api.request;

import com.destimatch.common.utils.BudgetLevel;
import com.destimatch.common.utils.TravelStyle;
import lombok.Data;

import java.util.List;

@Data
public class UpdatePreferencesRequest {
    private List<String> tags;
    private TravelStyle travelStyle;
    private BudgetLevel budgetLevel;
}
