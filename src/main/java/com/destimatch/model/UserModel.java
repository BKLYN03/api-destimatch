package com.destimatch.model;

import com.destimatch.common.utils.Location;
import com.mongodb.lang.Nullable;
import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.ArrayList;
import java.util.List;

@MongoEntity(collection = "users")
@Getter
@Setter
@AllArgsConstructor
public class UserModel extends PanacheMongoEntity {

    @BsonProperty("name")
    private String name;

    @BsonProperty("email")
    private String email;

    @BsonProperty("hashed_password")
    private String password;

    @BsonProperty("location")
    private Location location;

    @BsonProperty("phone")
    private String phone;

    @BsonProperty("preferences")
    @Nullable
    private List<String> preferences = new ArrayList<>();

    public UserModel() {}
}
