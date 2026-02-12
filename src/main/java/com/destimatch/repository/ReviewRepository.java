package com.destimatch.repository;

import com.destimatch.entity.ReviewEntity;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class ReviewRepository implements PanacheMongoRepository<ReviewEntity> {

    public List<ReviewEntity> findByDestinationId(String destinationId, int pageIndex, int pageSize) {
        return find("destinationId", Sort.descending("creationDate"), destinationId)
                .page(pageIndex, pageSize)
                .list();
    }
}
