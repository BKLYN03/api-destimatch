package com.destimatch.repository;

import com.destimatch.entity.DestinationEntity;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class DestinationRepository implements PanacheMongoRepository<DestinationEntity> {

    public List<DestinationEntity> findByTag(String tagName) {
        return list("tags", tagName);
    }

    // (?i) rend la regex insensible à la casse (Majuscule/minuscule)
    public List<DestinationEntity> searchByName(String name) {
        return list("name like ?1", "(?i).*" + name + ".*");
    }
}
