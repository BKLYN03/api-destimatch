package com.destimatch.service;

import com.destimatch.common.api.response.DestinationMatchResponse;
import com.destimatch.common.utils.BudgetLevel;
import com.destimatch.converter.DestinationConverter;
import com.destimatch.entity.DestinationEntity;
import com.destimatch.entity.UserEntity;
import com.destimatch.repository.DestinationRepository;
import com.destimatch.repository.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class MatchingService {

    @Inject
    UserRepository userRepository;
    @Inject
    DestinationRepository destinationRepository;

    public List<DestinationMatchResponse> findMatchesForUser(String userEmail) {
        UserEntity user = userRepository.find("email", userEmail).firstResult();
        if (user == null)
            return List.of();

        List<DestinationEntity> destinations = destinationRepository.listAll();

        return destinations.stream()
                .map(dest -> {
                    int score = calculateScore(user, dest);
                    return new DestinationMatchResponse(DestinationConverter.toResponse(dest), score);
                })
                .sorted(Comparator.comparingInt(DestinationMatchResponse::getMatchScore).reversed())
                .collect(Collectors.toList());
    }

    private int calculateScore(UserEntity user, DestinationEntity dest) {
        double totalScore = 0;

        /* ------- Les Tags (60% du calcul) ------- */
        List<String> userTags = user.getPreferences();
        List<String> destTags = dest.getTags();

        if (userTags != null && !userTags.isEmpty() && destTags != null) {
            long commonTags = userTags.stream().filter(destTags::contains).count();
            double matchRatio = (double) commonTags / userTags.size();
            totalScore += matchRatio * 60;
        } else
            totalScore += 30;

        /* ------- Budget 'intelligent' (30% du calcul) ------- */
        totalScore += calculateBudgetScore(user.getBudgetLevel(), dest.getAverageDailyCost());

        /* ------- Qualité (10% du calcul) ------- */
        if (dest.getRating() != null)
            totalScore += dest.getRating() * 2;

        return (int) Math.round(totalScore);
    }

    private double calculateBudgetScore(BudgetLevel userBudget, Double destCost) {
        if (userBudget == null || destCost == null)
            return 30.0;

        double maxBudget = 0;
        switch (userBudget) {
            case ECO:
                maxBudget = 80.0;
                break;
            case MODERATE:
                maxBudget = 180.0;
                break;
            case HIGH:
                maxBudget = 350.0;
                break;
            case LUXURY:
                return 30.0; // Le luxe n'a pas de limite
            default:
                return 30.0;
        }

        // Si le coût est dans le budget → 100% des points (30 points)
        if (destCost <= maxBudget)
            return 30.0;

        // Si ça dépasse, pénalité progressive
        // Ex: Budget 100, Coût 120. Dépassement = 0.2 (20%)
        double overflowRatio = (destCost - maxBudget) / maxBudget;

        // Si ça dépasse de plus de 50%, le score tombe à 0
        // Sinon on réduit proportionnellement
        if (overflowRatio >= 0.5)
            return 0.0;
        else
            return 30.0 * (1.0 - (overflowRatio * 2));
    }
}
