package com.destimatch.repository;

import com.destimatch.common.utils.BudgetLevel;
import com.destimatch.common.utils.Continent;
import com.destimatch.common.utils.TravelStyle;
import com.destimatch.entity.DestinationEntity;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class DestinationRepository implements PanacheMongoRepository<DestinationEntity> {

    public List<DestinationEntity> findByTag(String tagName) {
        return list("tags", tagName);
    }

    public List<String> getAllDistinctTags() {
        return mongoCollection().distinct("official_tags", String.class)
                .into(new ArrayList<>());
    }

    public List<DestinationEntity> search(String query, Continent continent, String tag,
                                          TravelStyle travelStyle, BudgetLevel budgetLevel) {
        Document queryDoc = new Document();

        if (query != null && !query.isEmpty())
            queryDoc.append("name", new Document("$regex", query).append("$options", "i"));

        if (continent != null)
            queryDoc.append("location.continent", continent.name());

        if (tag != null && !tag.isBlank()) {
            queryDoc.append("official_tags", tag);
        }

        if (travelStyle != null)
            queryDoc.append("compatible_styles", travelStyle.name());

        if (budgetLevel != null)
            queryDoc.append("budget_level", budgetLevel.name());

        return find(queryDoc).list();
    }
}
