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
                entity.getOfficialTags(),
                entity.getAiTags(),
                entity.getAiSummary(),
                entity.getAverageDailyCost(),
                entity.getBudgetLevel() != null ? entity.getBudgetLevel().name() : null,
                entity.getRating(),
                entity.getReviewCount(),
                entity.getBestMonths(),
                entity.getCompatibleStyles()
        );
    }
}