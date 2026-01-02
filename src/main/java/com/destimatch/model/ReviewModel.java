package com.destimatch.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewModel {

    @BsonProperty("author_pseudo")
    private String author;

    @BsonProperty("rating")
    private int rating;

    @BsonProperty("content")
    private String content;

    @BsonProperty("created_at")
    private Instant createdAt = Instant.now();

}
