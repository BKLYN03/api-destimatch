package com.destimatch.service;

import com.destimatch.common.api.response.DestinationResponse;
import com.destimatch.converter.DestinationConverter;
import com.destimatch.entity.FavoriteEntity;
import com.destimatch.entity.UserEntity;
import com.destimatch.repository.DestinationRepository;
import com.destimatch.repository.FavoriteRepository;
import com.destimatch.repository.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@ApplicationScoped
public class FavoriteService {

    @Inject
    UserRepository userRepository;
    @Inject
    FavoriteRepository favoriteRepository;
    @Inject
    DestinationRepository destinationRepository;

    public List<String> getTopContinents() {
        List<FavoriteEntity> allFavs = favoriteRepository.listAll();

        Map<String, Long> continentCounts = allFavs.stream()
            .map(fav -> destinationRepository.findById(new ObjectId(fav.getDestinationId())))
            .filter(Objects::nonNull)
            .map(dest -> dest.getLocation().getContinent())
            .filter(Objects::nonNull)
            .map(String::valueOf)
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        return continentCounts.entrySet().stream()
            .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
            .limit(4)
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
    }

    public List<DestinationResponse> getFavoriteDestinations(String email) {
        UserEntity user = userRepository.find("email", email).firstResult();
        if (user == null)
            throw  new NotFoundException("Utilisateur introuvable.");

        List<FavoriteEntity> favorites = favoriteRepository.findByUser(user.id.toString());

        return favorites.stream()
                .map(fav -> destinationRepository.findById(new ObjectId(fav.getDestinationId())))
                .filter(Objects::nonNull)
                .map(DestinationConverter::toResponse)
                .collect(Collectors.toList());
    }

    public void addFavorite(String email, String destinationId) {
        UserEntity user = userRepository.find("email", email).firstResult();
        if (user == null)
            throw new NotFoundException("Utilisateur introuvable.");

        if (destinationRepository.findById(new ObjectId(destinationId)) == null)
            throw new NotFoundException("Destination introuvable.");

        if (favoriteRepository.find(user.id.toString(), destinationId) == null) {
            FavoriteEntity newFav = new FavoriteEntity(user.id.toString(), destinationId);
            favoriteRepository.persist(newFav);
        }
    }

    public void removeFavorite(String email, String destinationId) {
        UserEntity user = userRepository.find("email", email).firstResult();
        if (user == null)
            throw  new NotFoundException("Utilisateur introuvable.");

        FavoriteEntity existing = favoriteRepository.find(user.id.toString(), destinationId);
        if (existing != null)
            favoriteRepository.delete(existing);
    }
}
