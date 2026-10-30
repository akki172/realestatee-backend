package com.example.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.PropertyCommentRequest;
import com.example.demo.dto.PropertyCommentResponse;
import com.example.demo.service.PropertyCommentService;

@RestController
@RequestMapping("/api/comments")
@CrossOrigin(origins = "http://localhost:5173")
public class PropertyCommentController {

    private final PropertyCommentService commentService;

    public PropertyCommentController(
            PropertyCommentService commentService) {

        this.commentService = commentService;
    }


    // =========================================
    // BUYER - ADD COMMENT
    // =========================================

    @PostMapping
    public ResponseEntity<PropertyCommentResponse> addComment(
            @RequestBody PropertyCommentRequest request) {

        return ResponseEntity.ok(
                commentService.addComment(request)
        );
    }


    // =========================================
    // GET COMMENTS FOR PROPERTY
    // =========================================

    @GetMapping("/property/{propertyId}")
    public ResponseEntity<List<PropertyCommentResponse>>
    getPropertyComments(
            @PathVariable Long propertyId) {

        return ResponseEntity.ok(
                commentService.getPropertyComments(
                        propertyId
                )
        );
    }


    // =========================================
    // SELLER - GET COMMENTS
    // =========================================

    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<PropertyCommentResponse>>
    getSellerComments(
            @PathVariable Long sellerId) {

        return ResponseEntity.ok(
                commentService.getSellerComments(
                        sellerId
                )
        );
    }


    // =========================================
    // BUYER - GET OWN COMMENTS
    // =========================================

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PropertyCommentResponse>>
    getBuyerComments(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                commentService.getBuyerComments(
                        userId
                )
        );
    }


    // =========================================
    // SELLER - REPLY
    // =========================================

    @PutMapping("/{commentId}/reply")
    public ResponseEntity<PropertyCommentResponse>
    replyToComment(
            @PathVariable Long commentId,
            @RequestParam Long sellerId,
            @RequestBody String replyText) {

        return ResponseEntity.ok(
                commentService.replyToComment(
                        commentId,
                        sellerId,
                        replyText
                )
        );
    }
}