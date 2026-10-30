package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.demo.dto.PropertyReviewRequest;
import com.example.demo.dto.PropertyReviewResponse;
import com.example.demo.entity.Property;
import com.example.demo.entity.PropertyReview;
import com.example.demo.entity.User;
import com.example.demo.repository.PropertyRepository;
import com.example.demo.repository.PropertyReviewRepository;
import com.example.demo.repository.UserRepository;

@Service
public class PropertyReviewService {

    private final PropertyReviewRepository reviewRepository;
    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;

    public PropertyReviewService(
            PropertyReviewRepository reviewRepository,
            PropertyRepository propertyRepository,
            UserRepository userRepository) {

        this.reviewRepository = reviewRepository;
        this.propertyRepository = propertyRepository;
        this.userRepository = userRepository;
    }


    // =========================================
    // ADD REVIEW
    // =========================================

    public PropertyReviewResponse addReview(
            PropertyReviewRequest request) {

        if (request.getRating() == null ||
                request.getRating() < 1 ||
                request.getRating() > 5) {

            throw new RuntimeException(
                    "Rating must be between 1 and 5"
            );
        }


        if (request.getComment() == null ||
                request.getComment().trim().isEmpty()) {

            throw new RuntimeException(
                    "Review comment cannot be empty"
            );
        }


        Property property =
                propertyRepository.findById(
                        request.getPropertyId()
                ).orElseThrow(() ->
                        new RuntimeException(
                                "Property not found"
                        )
                );


        User user =
                userRepository.findById(
                        request.getUserId()
                ).orElseThrow(() ->
                        new RuntimeException(
                                "User not found"
                        )
                );


        // Prevent duplicate review

        if (reviewRepository.existsByPropertyAndUser(
                property,
                user)) {

            throw new RuntimeException(
                    "You have already reviewed this property"
            );
        }


        PropertyReview review =
                new PropertyReview();

        review.setProperty(property);
        review.setUser(user);
        review.setRating(request.getRating());
        review.setComment(request.getComment().trim());
        review.setCreatedAt(LocalDateTime.now());


        PropertyReview saved =
                reviewRepository.save(review);


        return convertToResponse(saved);
    }


    // =========================================
    // GET REVIEWS FOR PROPERTY
    // =========================================

    public List<PropertyReviewResponse> getReviews(
            Long propertyId) {

        Property property =
                propertyRepository.findById(propertyId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Property not found"
                                )
                        );


        return reviewRepository
                .findByPropertyOrderByCreatedAtDesc(property)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }


    // =========================================
    // DELETE REVIEW
    // =========================================

    public void deleteReview(Long reviewId) {

        if (!reviewRepository.existsById(reviewId)) {

            throw new RuntimeException(
                    "Review not found"
            );
        }

        reviewRepository.deleteById(reviewId);
    }


    // =========================================
    // CONVERT ENTITY → RESPONSE
    // =========================================

    private PropertyReviewResponse convertToResponse(
            PropertyReview review) {

        String userName = "User";

        if (review.getUser() != null &&
                review.getUser().getFullName() != null) {

            userName =
                    review.getUser().getFullName();
        }


        return new PropertyReviewResponse(

                review.getId(),

                review.getRating(),

                review.getComment(),

                userName,

                review.getCreatedAt()

        );
    }
}