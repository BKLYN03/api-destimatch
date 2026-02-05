package com.destimatch.service;

import com.destimatch.common.api.request.SearchCriteria;
import com.destimatch.common.api.response.DestinationMatchResponse;
import com.destimatch.common.utils.BudgetLevel;
import com.destimatch.common.utils.TravelStyle;
import com.destimatch.converter.DestinationConverter;
import com.destimatch.entity.DestinationEntity;
import com.destimatch.entity.UserEntity;
import com.destimatch.repository.DestinationRepository;
import com.destimatch.repository.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class MatchingService {

    @Inject
    UserRepository userRepository;
    @Inject
    DestinationRepository destinationRepository;

    public List<DestinationMatchResponse> findMatchesForUser(String userEmail, SearchCriteria criteria) {
        UserEntity user = userRepository.find("email", userEmail).firstResult();
        if (user == null)
            return List.of();

        SearchCriteria safeCriteria = (criteria == null) ? new SearchCriteria() : criteria;

        List<DestinationEntity> destinations = destinationRepository.listAll();

        String targetContinent = (safeCriteria.getContinent() != null)
                ? safeCriteria.getContinent() : user.getPreferredContinent();

        if (targetContinent != null && !targetContinent.isBlank()) {
            destinations = destinations.stream()
                    .filter(d -> d.getLocation() != null
                            && targetContinent.equalsIgnoreCase(String.valueOf(d.getLocation().getContinent())))
                    .toList();
        }

        return destinations.stream()
                .map(dest -> {
                    int score = calculateScore(user, dest, safeCriteria);
                    return new DestinationMatchResponse(DestinationConverter.toResponse(dest), score);
                })
                .sorted(Comparator.comparingInt(DestinationMatchResponse::getMatchScore).reversed())
                .collect(Collectors.toList());
    }

    private int calculateScore(UserEntity user, DestinationEntity dest, SearchCriteria criteria) {
        double totalScore = 0;

        totalScore += calculateTagScore(user.getPreferences(), dest.getTags());

        BudgetLevel targetBudget = (criteria.getBudget() != null)
                ? criteria.getBudget()
                : user.getBudgetLevel();
        totalScore += calculateBudgetScore(targetBudget, dest.getAverageDailyCost());

        TravelStyle targetStyle = (criteria.getTravelStyle() != null)
                ? criteria.getTravelStyle()
                : user.getTravelStyle();
        totalScore += calculateStyleScore(targetStyle, dest.getCompatibleStyles());

        int targetMonth = (criteria.getMonth() != null)
                ? criteria.getMonth()
                : LocalDate.now().getMonthValue();
        totalScore += calculateSeasonScore(dest.getBestMonths(), targetMonth);

        if (dest.getRating() != null)
            totalScore += dest.getRating();

        return (int) Math.round(totalScore);
    }

    private double calculateTagScore(List<String> userTags, List<String> destTags) {
        if (userTags == null || userTags.isEmpty())
            return 20.0;
        if (destTags == null || destTags.isEmpty())
            return 0.0;

        long commonTags = userTags.stream().filter(destTags::contains).count();

        double ratio = (double) commonTags / userTags.size();
        return ratio * 40.0;
    }

    private double calculateBudgetScore(BudgetLevel userBudget, Double destCost) {
        if (userBudget == null || destCost == null)
            return 25.0;

        double maxBudget;
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
                return 25.0; // Le luxe n'a pas de limite
            default:
                return 25.0;
        }

        // Si le coût est dans le budget → 100% des points (30 points)
        if (destCost <= maxBudget)
            return 25.0;

        // Si ça dépasse, pénalité progressive
        // Ex: Budget 100, Coût 120. Dépassement = 0.2 (20%)
        double overflowRatio = (destCost - maxBudget) / maxBudget;

        // Si ça dépasse de plus de 50%, le score tombe à 0
        // Sinon on réduit proportionnellement
        if (overflowRatio >= 0.5)
            return 0.0;

        return 25.0 * (1.0 - (overflowRatio * 2));
    }

    private double calculateStyleScore(TravelStyle userStyle, List<TravelStyle> destStyles) {
        if (userStyle == null)
            return 10.0;
        if (destStyles == null || destStyles.isEmpty())
            return 10.0;

        return destStyles.contains(userStyle) ? 20.0 : 0.0;
    }

    private double calculateSeasonScore(List<Integer> bestMonths, int monthToCheck) {
        if (bestMonths == null || bestMonths.isEmpty())
            return 15.0;

        return bestMonths.contains(monthToCheck) ? 15.0 : 0.0;
    }
}
