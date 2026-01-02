package com.destimatch.common.api.response;

import com.destimatch.common.utils.Location;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class UserResponse {
    private String id;
    private String name;
    private String email;
    private Location location;
    private String phone;
    private List<String> preferences;
}
