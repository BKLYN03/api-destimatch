package com.destimatch.converter;

import com.destimatch.common.api.response.UserResponse;
import com.destimatch.entity.UserEntity;

public class UserConverter {

    public static UserResponse toResponse(UserEntity entity) {
        return new UserResponse(
                entity.id.toString(),
                entity.getName(),
                entity.getEmail(),
                entity.getPhone(),
                entity.getLocation(),
                entity.getPreferences(),
                entity.getTravelStyle(),
                entity.getBudgetLevel(),
                entity.getWishList()
        );
    }
}
