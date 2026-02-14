package com.destimatch.service;

import com.destimatch.common.api.request.CreateDestinationRequest;
import com.destimatch.common.api.response.DestinationResponse;
import com.destimatch.common.utils.BudgetLevel;
import com.destimatch.common.utils.Continent;
import com.destimatch.common.utils.TravelStyle;
import com.destimatch.converter.DestinationConverter;
import com.destimatch.entity.DestinationEntity;
import com.destimatch.entity.ReviewEntity;
import com.destimatch.repository.DestinationRepository;
import com.destimatch.repository.ReviewRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.bson.types.ObjectId;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class DestinationService {

    @Inject
    DestinationRepository destinationRepository;
    @Inject
    ReviewRepository reviewRepository;

    public DestinationResponse createDestination(CreateDestinationRequest request) {
        DestinationEntity dest = new DestinationEntity();

        dest.setName(request.getName());
        dest.setDescription(request.getDescription());
        dest.setImages(request.getImages());
        dest.setLocation(request.getLocation());

        dest.setOfficialTags(request.getOfficialTags());

        dest.setAverageDailyCost(request.getAverageDailyCost());
        dest.setBudgetLevel(BudgetLevel.fromCost(request.getAverageDailyCost()));
        dest.setBestMonths(request.getBestMonths());
        dest.setCompatibleStyles(request.getCompatibleStyles());

        dest.setRating(0.0);
        dest.setReviewCount(0);

        destinationRepository.persist(dest);
        return DestinationConverter.toResponse(dest);
    }

    public List<DestinationResponse> getAllDestinations() {
        List<DestinationEntity> destinations = destinationRepository.listAll();
        return destinations.stream()
                .map(DestinationConverter::toResponse)
                .collect(Collectors.toList());
    }

    public List<String> getAvailableTags() {
        List<String> tags = destinationRepository.getAllDistinctTags();

        Collections.sort(tags);
        return tags;
    }

    public List<DestinationResponse> searchDestinations(String q, String continentStr, String tag,
                                                        String styleStr, String budgetStr) {
        Continent continent = null;
        if (continentStr != null && !continentStr.isBlank()) {
            try {
                continent = Continent.valueOf(continentStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(e);
            }
        }

        TravelStyle travelStyle = null;
        if (styleStr != null && !styleStr.isBlank()) {
            try {
                travelStyle = TravelStyle.valueOf(styleStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(e);
            }
        }

        BudgetLevel budgetLevel = null;
        if (budgetStr != null && !budgetStr.isBlank()) {
            try {
                budgetLevel = BudgetLevel.valueOf(budgetStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(e);
            }
        }

        return destinationRepository.search(q, continent, tag, travelStyle, budgetLevel)
                .stream().map(DestinationConverter::toResponse)
                .collect(Collectors.toList());
    }

    public DestinationResponse getDestinationById(String id) {
        DestinationEntity dest = destinationRepository.findById(new ObjectId(id));
        if (dest == null)
            throw new NotFoundException("Destination introuvable avec l'ID : " + id);

        return DestinationConverter.toResponse(dest);
    }

    public DestinationResponse updateDestination(String id, CreateDestinationRequest request) {
        DestinationEntity dest = destinationRepository.findById(new ObjectId(id));
        if (dest == null)
            throw new NotFoundException("Destination introuvable.");

        dest.setName(request.getName());
        dest.setDescription(request.getDescription());
        dest.setImages(request.getImages());
        dest.setLocation(request.getLocation());
        dest.setOfficialTags(request.getOfficialTags());
        dest.setAverageDailyCost(request.getAverageDailyCost());
        dest.setBestMonths(request.getBestMonths());
        dest.setCompatibleStyles(request.getCompatibleStyles());

        destinationRepository.update(dest);
        return DestinationConverter.toResponse(dest);
    }

    public void deleteDestination(String id) {
        boolean deleted = destinationRepository.deleteById(new ObjectId(id));
        if (!deleted)
            throw new NotFoundException("Destination introuvable");
    }

    public void updateDestinationAiStats(String destinationId) {
        DestinationEntity dest = destinationRepository.findById(new ObjectId(destinationId));
        List<ReviewEntity> reviews = reviewRepository.list("destinationId", destinationId);

        if (dest == null || reviews.isEmpty()) return;

        dest.aiScoreCleanliness = calculateAspectScore(reviews, "CLEANLINESS");
        dest.aiScorePrice = calculateAspectScore(reviews, "PRICE");
        dest.aiScoreVibe = calculateAspectScore(reviews, "VIBE");

        dest.communityTags.clear();
        if (dest.aiScoreCleanliness > 80)
            dest.communityTags.add("Hygiène Top");
        if (dest.aiScoreVibe < 30)
            dest.communityTags.add("Bruyant");
        if (dest.aiScorePrice > 80)
            dest.communityTags.add("Bon Plan");

        destinationRepository.update(dest);
    }

    private double calculateAspectScore(List<ReviewEntity> reviews, String aspect) {
        long total = reviews.stream().filter(r -> r.getAspectSentiments().containsKey(aspect)).count();
        if (total == 0) return 0.0;

        long positive = reviews.stream()
                .filter(r -> "POSITIVE".equals(r.getAspectSentiments().get(aspect)))
                .count();

        return ((double) positive / total) * 100.0;
    }
}
