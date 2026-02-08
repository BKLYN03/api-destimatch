package com.destimatch.service;

import com.destimatch.common.api.request.AddReviewRequest;
import com.destimatch.common.api.response.ReviewResponse;
import com.destimatch.common.exception.ConflictException;
import com.destimatch.converter.ReviewConverter;
import com.destimatch.entity.DestinationEntity;
import com.destimatch.entity.ReviewEntity;
import com.destimatch.entity.UserEntity;
import com.destimatch.repository.DestinationRepository;
import com.destimatch.repository.ReviewRepository;
import com.destimatch.repository.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.WebApplicationException;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class ReviewService {

    @Inject
    ReviewRepository reviewRepository;
    @Inject
    DestinationRepository destinationRepository;
    @Inject
    UserRepository userRepository;

    public void addReview(String userEmail, String destinationId, AddReviewRequest addReviewRequest) {
        UserEntity user = userRepository.find("email", userEmail).firstResult();

        // On compte combien d'avis cet utilisateur a déjà mis sur cette destination
        long existingReviews = reviewRepository.count("destinationId = ?1 and userId = ?2", destinationId, user.id.toString());
        if (existingReviews > 0)
            throw new WebApplicationException("Vous avez déjà noté cette destination.", 409);

        DestinationEntity destination = destinationRepository.findById(new ObjectId(destinationId));
        if (destination == null)
            throw new NotFoundException("Destination inconnue.");

        ReviewEntity review = new ReviewEntity();
        review.setAuthor(user.getName());
        review.setUserId(user.id.toString());
        review.setDestinationId(destinationId);
        review.setRating(addReviewRequest.getRating());
        review.setContent(addReviewRequest.getContent());

        reviewRepository.persist(review);
        updateDestinationStats(destination, addReviewRequest.getRating());
    }

    private void updateDestinationStats(DestinationEntity dest, int newRating) {
        double currentTotal = dest.getRating() * dest.getReviewCount();
        int newCount = dest.getReviewCount() + 1;

        double newAverage = (currentTotal + newRating) / newCount;

        newAverage = Math.round(newAverage * 10.0) / 10.0;

        dest.setRating(newAverage);
        dest.setReviewCount(newCount);

        destinationRepository.update(dest);
    }

    public List<ReviewResponse> getReviews(String destinationId, int pageIndex, int PageSize) {
        return reviewRepository.findByDestinationId(destinationId, pageIndex, PageSize)
                .stream()
                .map(ReviewConverter::toResponse)
                .collect(Collectors.toList());
    }

    public void deleteReview(String reviewId) {
        ReviewEntity review = reviewRepository.findById(new ObjectId(reviewId));
        if (review == null)
            throw new NotFoundException("Avis introuvable.");

        DestinationEntity dest = destinationRepository.findById(new ObjectId(review.getDestinationId()));
        if (dest != null)
            recalculateRatingAfterDeletion(dest, review.getRating());

        reviewRepository.delete(review);
    }

    private void recalculateRatingAfterDeletion(DestinationEntity dest, int ratingToRemove) {
        double currentTotal = dest.getRating() * dest.getReviewCount();
        int newCount = dest.getReviewCount() - 1;

        if (newCount <= 0) {
            dest.setRating(0.0);
            dest.setReviewCount(0);
        } else {
            double newTotal = currentTotal - ratingToRemove;
            double newAverage = newTotal / newCount;

            newAverage = Math.round(newAverage * 10.0) / 10.0;

            dest.setRating(Math.max(0.0, newAverage));
            dest.setReviewCount(newCount);
        }

        destinationRepository.update(dest);
    }
}
