package com.destimatch.model;

import com.destimatch.common.utils.Category;
import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonProperty;

@MongoEntity(collection = "tags")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TagModel extends PanacheMongoEntity {

    @BsonProperty("name")
    private String name;

    @BsonProperty("category")
    private Category category;

    @BsonProperty("description")
    private String description;

    @BsonProperty("color_hex")
    private String colorHex;
}
