package com.destimatch.converter;

import com.destimatch.common.api.response.UserResponse;
import com.destimatch.common.utils.Continent;
import com.destimatch.entity.UserEntity;

import java.util.stream.Collectors;

public class UserConverter {

    public static UserResponse toResponse(UserEntity entity) {
        return new UserResponse(
                entity.id.toString(),
                entity.getName(),
                entity.getEmail(),
                // entity.getPhone(),
                entity.getLocation(),
                entity.getRoles(),
                entity.getPreferences(),
                entity.getTravelStyle(),
                entity.getBudgetLevel(),
                entity.getFavoriteContinents() != null ?
                        entity.getFavoriteContinents().stream()
                                .map(Continent::getLabel)
                                .collect(Collectors.toList())
                        : null
        );
    }
}
