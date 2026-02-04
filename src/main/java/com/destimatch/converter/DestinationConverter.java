package com.destimatch.converter;

import com.destimatch.common.api.response.DestinationResponse;
import com.destimatch.entity.DestinationEntity;

public class DestinationConverter {

    public static DestinationResponse toResponse(DestinationEntity entity) {
        if (entity == null)
            return null;

        return new DestinationResponse(
                entity.id.toString(),
                entity.getName(),
                entity.getDescription(),
                entity.getImages(),
                entity.getLocation(),
                entity.getTags(),
                entity.getAverageDailyCost(),
                entity.getRating(),
                entity.getReviewCount(),
                entity.getBestMonths()
        );
    }
}