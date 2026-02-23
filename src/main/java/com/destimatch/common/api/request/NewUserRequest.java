package com.destimatch.common.api.request;

import com.destimatch.common.utils.Location;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonProperty;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewUserRequest {
    private String name;
    private String email;
    private String password;
    // private String phone;
    private Location location;

    @BsonProperty("admin_secret")
    private String adminSecret;
}
