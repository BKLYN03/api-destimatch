package com.destimatch.common.api.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DestinationMatchResponse extends DestinationResponse {

    private int matchScore;

    public DestinationMatchResponse(DestinationResponse original, int score) {
        super(
                original.getId(),
                original.getName(),
                original.getDescription(),
                original.getImages(),
                original.getLocation(),
                original.getTags(),
                original.getAverageDailyCost(),
                original.getRating(),
                original.getReviewCount(),
                original.getBestMonths()
        );
        this.matchScore = score;
    }
}
