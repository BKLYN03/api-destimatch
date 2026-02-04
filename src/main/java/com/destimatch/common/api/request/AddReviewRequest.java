package com.destimatch.common.api.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddReviewRequest {
    private Integer rating;
    private String content;
}
