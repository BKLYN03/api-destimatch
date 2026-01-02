package com.destimatch.model;

import com.destimatch.common.utils.Location;
import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.ArrayList;
import java.util.List;

@MongoEntity(collection = "destinations")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DestinationModel extends PanacheMongoEntity {

    @BsonProperty("name")
    private String name;

    @BsonProperty("description")
    private String description;

    @BsonProperty("average_price")
    private double averagePrice;

    @BsonProperty("rating")
    private double rating;

    @BsonProperty("location")
    private Location location;

    @BsonProperty("image_url")
    private String imageUrl;

    @BsonProperty("tags")
    private List<String> tags = new ArrayList<>();

    @BsonProperty("reviews")
    private List<ReviewModel> reviews = new ArrayList<>();
}
