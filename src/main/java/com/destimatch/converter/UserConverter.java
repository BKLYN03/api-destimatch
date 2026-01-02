package com.destimatch.converter;

import com.destimatch.common.api.response.UserResponse;
import com.destimatch.entity.UserEntity;
import com.destimatch.model.UserModel;

public class UserConverter {


    public static UserResponse toResponse(UserModel user) {
        if (user == null) return null;

        return new UserResponse(
                user.id.toString(),
                user.getName(),
                user.getEmail(),
                user.getLocation(),
                user.getPhone(),
                user.getPreferences()
        );
    }

    public static UserEntity toEntity(UserModel model) {
        return new UserEntity(
                model.getName(),
                model.getEmail(),
                model.getPassword(),
                model.getLocation(),
                model.getPhone()
        );
    }

    public static UserModel toModel(UserEntity entity) {
        UserModel model = new UserModel();
        model.setName(entity.getName());
        model.setEmail(entity.getEmail());
        model.setPassword(entity.getPassword());
        model.setLocation(entity.getLocation());
        model.setPhone(entity.getPhone());

        return model;
    }
}
