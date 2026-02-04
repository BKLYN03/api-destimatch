package com.destimatch.service;

import com.destimatch.common.api.request.CreateDestinationRequest;
import com.destimatch.common.api.response.DestinationResponse;
import com.destimatch.converter.DestinationConverter;
import com.destimatch.entity.DestinationEntity;
import com.destimatch.repository.DestinationRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class DestinationService {

    @Inject
    DestinationRepository destinationRepository;

    public DestinationResponse createDestination(CreateDestinationRequest request) {
        DestinationEntity dest = new DestinationEntity();

        // Mapping manuel (ou via MapStruct si tu l'utilises)
        dest.setName(request.getName());
        dest.setDescription(request.getDescription());
        dest.setImages(request.getImages());
        dest.setLocation(request.getLocation());

        // Critères de matching
        dest.setTags(request.getTags());
        dest.setAverageDailyCost(request.getAverageDailyCost());
        dest.setBestMonths(request.getBestMonths());

        // Initialisation des stats sociales
        dest.setRating(0.0);
        dest.setReviewCount(0);

        destinationRepository.persist(dest);
        return DestinationConverter.toResponse(dest);
    }

    public List<DestinationResponse> getAllDestinations(String query) {
        List<DestinationEntity> destinations;

        if (query != null && !query.isBlank())
            destinations = destinationRepository.searchByName(query);
        else
            destinations = destinationRepository.listAll();

        return destinations.stream()
                .map(DestinationConverter::toResponse)
                .collect(Collectors.toList());
    }

    public DestinationResponse getDestinationById(String id) {
        DestinationEntity dest = destinationRepository.findById(new ObjectId(id));
        if (dest == null)
            throw new NotFoundException("Destination introuvable avec l'ID : " + id);

        return DestinationConverter.toResponse(dest);
    }
}
