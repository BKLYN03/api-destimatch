package com.destimatch.converter;

import com.destimatch.common.api.response.ReviewResponse;
import com.destimatch.entity.ReviewEntity;

public class ReviewConverter {

    public static ReviewResponse toResponse(ReviewEntity reviewEntity) {
        return new ReviewResponse(
                reviewEntity.id.toString(),
                reviewEntity.getAuthor(),
                reviewEntity.getRating(),
                reviewEntity.getContent(),
                reviewEntity.getCreationDate()
        );
    }
}
