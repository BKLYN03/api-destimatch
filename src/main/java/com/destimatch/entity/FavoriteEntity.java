package com.destimatch.entity;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.time.Instant;

@MongoEntity(collection = "favorites")
@Getter
@Setter
@NoArgsConstructor
public class FavoriteEntity extends PanacheMongoEntity {

    @BsonProperty("user_id")
    private String userId;

    @BsonProperty("destination_id")
    private String destinationId;

    @BsonProperty("added_at")
    public Instant addedAt = Instant.now();

    public FavoriteEntity(String userId, String destinationId) {
        this.userId = userId;
        this.destinationId = destinationId;
    }
}
