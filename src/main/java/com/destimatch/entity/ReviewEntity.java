package com.destimatch.entity;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;
import org.bson.codecs.pojo.annotations.BsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@MongoEntity(collection = "reviews")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewEntity extends PanacheMongoEntity {

    @BsonProperty("author_pseudo")
    private String author;

    @BsonProperty("user_id")
    private String userId;

    @BsonProperty("destination_id")
    private String destinationId;

    @BsonProperty("rating")
    private int rating;

    @BsonProperty("content")
    private String content;

    @BsonProperty("creation_date")
    private Instant creationDate = Instant.now();
}
