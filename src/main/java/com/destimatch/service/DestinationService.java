package com.destimatch.service;

import com.destimatch.common.api.request.CreateDestinationRequest;
import com.destimatch.common.api.response.DestinationResponse;
import com.destimatch.common.utils.BudgetLevel;
import com.destimatch.common.utils.Continent;
import com.destimatch.common.utils.TravelStyle;
import com.destimatch.converter.DestinationConverter;
import com.destimatch.entity.DestinationEntity;
import com.destimatch.repository.DestinationRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.WebApplicationException;
import org.bson.types.ObjectId;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class DestinationService {

    @Inject
    DestinationRepository destinationRepository;

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
        dest.setAiTags(new HashMap<>());

        destinationRepository.persist(dest);
        return DestinationConverter.toResponse(dest);
    }

    public List<DestinationResponse> getAllDestinations() {
        List<DestinationEntity> destinations = destinationRepository.listAll();
        return destinations.stream()
                .map(DestinationConverter::toResponse)
                .collect(Collectors.toList());
    }

    public List<DestinationResponse> searchDestinations(String q, String continentStr, String styleStr,
                                                        String budgetStr) {
        Continent continent = (continentStr != null && !continentStr.isEmpty())
                ? Continent.fromString(continentStr)
                : null;

        TravelStyle travelStyle = (styleStr != null && !styleStr.isEmpty())
                ? TravelStyle.valueOf(styleStr.toUpperCase())
                : null;

        BudgetLevel budgetLevel = (budgetStr != null && !budgetStr.isEmpty())
                ? BudgetLevel.valueOf(budgetStr.toUpperCase())
                : null;

        return destinationRepository.search(q, continent, travelStyle, budgetLevel)
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
}
