package com.destimatch.repository;

import com.destimatch.entity.TagEntity;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TagRepository implements PanacheMongoRepository<TagEntity> {
}
