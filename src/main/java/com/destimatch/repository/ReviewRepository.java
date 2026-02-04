package com.destimatch.repository;

import com.destimatch.entity.ReviewEntity;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class ReviewRepository implements PanacheMongoRepository<ReviewEntity> {

    public List<ReviewEntity> findByDestinationId(String destinationId, int pageIndex, int pageSize) {
        return find("destinationId = ?1 order by creationDate desc", destinationId)
                .page(pageIndex, pageSize)
                .list();
    }
}
