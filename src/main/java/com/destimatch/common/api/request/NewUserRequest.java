package com.destimatch.common.api.request;

import com.destimatch.common.utils.Location;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewUserRequest {
    private String name;
    private String email;
    private String password;
    private Location location;
    private String phone;
}
