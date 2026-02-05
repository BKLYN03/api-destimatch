package com.destimatch.common.api.request;

import com.destimatch.common.utils.BudgetLevel;
import com.destimatch.common.utils.TravelStyle;
import lombok.Data;

@Data
public class SearchCriteria {
    private String continent;
    private TravelStyle travelStyle;
    private BudgetLevel budget;
    private Integer month;
}
