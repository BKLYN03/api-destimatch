package com.destimatch.common.api.response;

import com.destimatch.common.utils.BudgetLevel;
import com.destimatch.common.utils.Location;
import com.destimatch.common.utils.TravelStyle;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class UserResponse {
    private String id;
    private String name;
    private String email;
    private String phone;
    private Location location;
    private List<String> roles;
    private List<String> preferences;
    private TravelStyle travelStyle;
    private BudgetLevel budgetLevel;
    private List<String> favoriteContinents;
}
