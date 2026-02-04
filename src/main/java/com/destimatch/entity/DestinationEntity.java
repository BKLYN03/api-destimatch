package com.destimatch.entity;

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
@NoArgsConstructor
@AllArgsConstructor
public class DestinationEntity extends PanacheMongoEntity {

    @BsonProperty("name")
    private String name; // ex: "Ubud, Bali", "Santorini"

    @BsonProperty("description")
    private String description; // texte vendeur, description marketing-axed

    // Liste d'URLs — le premier URL sera utilisé comme couverture
    @BsonProperty("images")
    private List<String> images = new ArrayList<>();

    @BsonProperty("location")
    private Location location; // { lat, lon, city, etc. }

    @BsonProperty("tags")
    private List<String> tags = new ArrayList<>();

    @BsonProperty("average_daily_cost")
    private Double averageDailyCost;

    @BsonProperty("rating")
    private Double rating = 0.0;

    @BsonProperty("review_count")
    private Integer reviewCount = 0;

    // Liste des mois conseillés (1=Janvier, 12=Décembre)
    @BsonProperty("best_months")
    private List<Integer> bestMonths;
}
