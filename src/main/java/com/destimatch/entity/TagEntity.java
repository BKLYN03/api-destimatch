package com.destimatch.entity;

import com.destimatch.common.utils.Category;
import io.quarkus.mongodb.panache.PanacheMongoEntity;
import org.bson.codecs.pojo.annotations.BsonProperty;
import io.quarkus.mongodb.panache.common.MongoEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@MongoEntity(collection = "tags")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TagEntity extends PanacheMongoEntity {

    @BsonProperty("name")
    private String name;

    @BsonProperty("category")
    private Category category;

    @BsonProperty("description")
    private String description;

    @BsonProperty("color_hex")
    private String colorHex;
}
