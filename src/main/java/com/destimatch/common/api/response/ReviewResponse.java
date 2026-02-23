package com.destimatch.common.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class ReviewResponse {
    private String id;
    private String author; // Son nom
    private String userEmail;
    private Integer rating;
    private String content;
    private Instant date;
    private Map<String, String> aspectSentiments;
    private List<String> aiKeywords;
}
