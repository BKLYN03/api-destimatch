package com.destimatch.common.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class ReviewResponse {
    private String id;
    private String authorPseudo; // Son nom
    private Integer rating;
    private String content;
    private Instant date;
}
