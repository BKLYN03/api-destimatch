package com.destimatch.entity;

import com.destimatch.common.utils.Location;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class UserEntity {
    private String name;
    private String email;
    private String password;
    private Location location;
    private String phone;
}
