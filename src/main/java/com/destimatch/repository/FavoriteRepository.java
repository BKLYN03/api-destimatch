package com.destimatch.repository;

import com.destimatch.entity.FavoriteEntity;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class FavoriteRepository implements PanacheMongoRepository<FavoriteEntity> {

    public List<FavoriteEntity> findByUser(String userId) {
        return list("userId", userId);
    }

    public FavoriteEntity find(String userId, String destinationId) {
        return find("userId = ?1 and destinationId = ?2", userId, destinationId).firstResult();
    }
}
