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
import java.util.HashSet;
import java.util.Set;

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

        destinations = destinations.stream()
                .filter(d -> isContinentMatch(user, safeCriteria, d))
                .toList();

        return destinations.stream()
                .map(dest -> {
                    int score = calculateScore(user, dest, safeCriteria);
                    return new DestinationMatchResponse(DestinationConverter.toResponse(dest), score);
                })
                .sorted(Comparator.comparingInt(DestinationMatchResponse::getMatchScore).reversed())
                .collect(Collectors.toList());
    }

    private boolean isContinentMatch(UserEntity user, SearchCriteria criteria, DestinationEntity dest) {
        if (criteria.getContinent() != null && !criteria.getContinent().isBlank())
            return dest.getLocation().getContinent().getLabel().equalsIgnoreCase(criteria.getContinent());

        if (user.getFavoriteContinents() != null && !user.getFavoriteContinents().isEmpty())
            return user.getFavoriteContinents().contains(dest.getLocation().getContinent());

        return true;
    }

    private int calculateScore(UserEntity user, DestinationEntity dest, SearchCriteria criteria) {
        double totalScore = 0;

        totalScore += calculateTagScore(user.getPreferences(), dest);

        BudgetLevel targetBudget = (criteria.getBudget() != null) ? criteria.getBudget() : user.getBudgetLevel();
        totalScore += calculateBudgetScore(targetBudget, dest.getAverageDailyCost());

        TravelStyle targetStyle = (criteria.getTravelStyle() != null) ? criteria.getTravelStyle() : user.getTravelStyle();
        totalScore += calculateStyleScore(targetStyle, dest.getCompatibleStyles());

        int targetMonth = (criteria.getMonth() != null) ? criteria.getMonth() : LocalDate.now().getMonthValue();
        totalScore += calculateSeasonScore(dest.getBestMonths(), targetMonth);

        if (dest.getRating() != null)
            totalScore += dest.getRating() * 2;

        totalScore += calculateAiQualityBonus(dest, user);

        return (int) Math.round(Math.clamp(totalScore, 0.0, 100.0));
    }

    private double calculateTagScore(List<String> userTags, DestinationEntity dest) {
        if (userTags == null || userTags.isEmpty())
            return 10.0;

        Set<String> allDestinationTags = new HashSet<>();

        if (dest.getOfficialTags() != null) {
            dest.getOfficialTags().forEach(tag -> allDestinationTags.add(tag.toLowerCase()));
        }

        if (dest.getCommunityTags() != null) {
            dest.getCommunityTags().forEach(tag -> allDestinationTags.add(tag.toLowerCase()));
        }

        if (allDestinationTags.isEmpty())
            return 0.0;

        long commonTags = userTags.stream()
                .map(String::toLowerCase)
                .filter(allDestinationTags::contains)
                .count();

        double ratio = (double) commonTags / userTags.size();
        return ratio * 40.0;
    }

    private double calculateBudgetScore(BudgetLevel userBudget, Double destCost) {
        if (userBudget == null || destCost == null)
            return 25.0;

        double maxBudget;
        switch (userBudget) {
            case ECO:
                maxBudget = 60.0;
                break;
            case MODERATE:
                maxBudget = 160.0;
                break;
            case HIGH:
                maxBudget = 400.0;
                break;
            case LUXURY:
                return 30.0;
            default:
                return 20.0;
        }

        if (destCost <= maxBudget)
            return 30.0;

        double overflowRatio = (destCost - maxBudget) / maxBudget;
        double score = 30.0 * (1.0 - (overflowRatio * 2));
        return Math.max(0, score);
    }

    private double calculateStyleScore(TravelStyle userStyle, List<TravelStyle> destStyles) {
        if (userStyle == null)
            return 10.0;
        if (destStyles == null || destStyles.isEmpty())
            return 5.0;

        return destStyles.contains(userStyle) ? 20.0 : 0.0;
    }

    private double calculateSeasonScore(List<Integer> bestMonths, int monthToCheck) {
        if (bestMonths == null || bestMonths.isEmpty())
            return 10.0;

        return bestMonths.contains(monthToCheck) ? 15.0 : 0.0;
    }

    private double calculateAiQualityBonus(DestinationEntity dest, UserEntity user) {
        if (dest.getReviewCount() == null || dest.getReviewCount() < 3) {
            return 0.0;
        }

        double bonus = 0.0;

        if (dest.getAiScoreCleanliness() != null && dest.getAiScoreCleanliness() > 75.0) {
            bonus += 5.0; 
        } else if (dest.getAiScoreCleanliness() != null && dest.getAiScoreCleanliness() < 40.0) {
            bonus -= 10.0;
        }

        if (dest.getAiScorePrice() != null && dest.getAiScorePrice() > 80.0) {
            if (user.getBudgetLevel() == BudgetLevel.ECO || user.getBudgetLevel() == BudgetLevel.MODERATE) {
                bonus += 5.0;
            }
        }

        if (dest.getAiScoreVibe() != null && dest.getAiScoreVibe() > 80.0) {
            bonus += 3.0;
        }

        return Math.clamp(bonus, -10.0, 15.0);
    }
}
