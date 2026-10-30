package com.example.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.PropertyReviewRequest;
import com.example.demo.dto.PropertyReviewResponse;
import com.example.demo.service.PropertyReviewService;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "http://localhost:5173")
public class PropertyReviewController {

    private final PropertyReviewService reviewService;

    public PropertyReviewController(
            PropertyReviewService reviewService) {

        this.reviewService = reviewService;
    }


    // =========================================
    // ADD REVIEW
    // =========================================

    @PostMapping
    public ResponseEntity<PropertyReviewResponse> addReview(
            @RequestBody PropertyReviewRequest request) {

        return ResponseEntity.ok(
                reviewService.addReview(request)
        );
    }


    // =========================================
    // GET REVIEWS FOR PROPERTY
    // =========================================

    @GetMapping("/property/{propertyId}")
    public ResponseEntity<List<PropertyReviewResponse>> getReviews(
            @PathVariable Long propertyId) {

        return ResponseEntity.ok(
                reviewService.getReviews(propertyId)
        );
    }


    // =========================================
    // DELETE REVIEW
    // =========================================

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable Long reviewId) {

        reviewService.deleteReview(reviewId);

        return ResponseEntity.noContent().build();
    }
}