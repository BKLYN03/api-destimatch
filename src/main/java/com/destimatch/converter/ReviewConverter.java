package com.destimatch.converter;

import com.destimatch.common.api.response.ReviewResponse;
import com.destimatch.entity.ReviewEntity;

import java.util.ArrayList;
import java.util.HashMap;

public class ReviewConverter {

    public static ReviewResponse toResponse(ReviewEntity reviewEntity) {
        return new ReviewResponse(
                reviewEntity.id.toString(),
                reviewEntity.getAuthor(),
                reviewEntity.getUserEmail(),
                reviewEntity.getRating(),
                reviewEntity.getContent(),
                reviewEntity.getCreationDate(),
                reviewEntity.getAspectSentiments() != null
                        ? reviewEntity.getAspectSentiments() : new HashMap<>(),
                reviewEntity.getAiKeywords() != null
                        ? reviewEntity.getAiKeywords() : new ArrayList<>()
        );
    }
}
