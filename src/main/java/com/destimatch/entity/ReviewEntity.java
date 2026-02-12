package com.destimatch.entity;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;
import org.bson.codecs.pojo.annotations.BsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@MongoEntity(collection = "reviews")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewEntity extends PanacheMongoEntity {

    @BsonProperty("author")
    private String author;

    @BsonProperty("user_email")
    private String userEmail;

    @BsonProperty("destination_id")
    private String destinationId;

    @BsonProperty("rating")
    private int rating;

    @BsonProperty("content")
    private String content;

    @BsonProperty("creation_date")
    private Instant creationDate = Instant.now();

    @BsonProperty("aspect_sentiments")
    private Map<String, String> aspectSentiments = new HashMap<>();

    @BsonProperty("ai_keywords")
    private List<String> aiKeywords;
}
