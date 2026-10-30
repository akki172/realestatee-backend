package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.demo.dto.PropertyCommentRequest;
import com.example.demo.dto.PropertyCommentResponse;
import com.example.demo.entity.Property;
import com.example.demo.entity.PropertyComment;
import com.example.demo.entity.User;
import com.example.demo.repository.PropertyCommentRepository;
import com.example.demo.repository.PropertyRepository;
import com.example.demo.repository.UserRepository;

@Service
public class PropertyCommentService {

    private final PropertyCommentRepository commentRepository;
    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    public PropertyCommentService(
            PropertyCommentRepository commentRepository,
            PropertyRepository propertyRepository,
            UserRepository userRepository,
            NotificationService notificationService) {

        this.commentRepository = commentRepository;
        this.propertyRepository = propertyRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    // =========================================
    // ADD COMMENT
    // =========================================

    public PropertyCommentResponse addComment(
            PropertyCommentRequest request) {

        if (request.getComment() == null ||
                request.getComment().trim().isEmpty()) {

            throw new RuntimeException(
                    "Comment cannot be empty"
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

        PropertyComment comment =
                new PropertyComment();

        comment.setProperty(property);
        comment.setUser(user);
        comment.setComment(
                request.getComment().trim()
        );
        comment.setCreatedAt(
                LocalDateTime.now()
        );

        PropertyComment saved =
                commentRepository.save(comment);
        
     // =========================================
     // NOTIFY SELLER
     // =========================================

     if (property.getSeller() != null &&
             property.getSeller().getId() != null &&
             !property.getSeller()
                     .getId()
                     .equals(user.getId())) {

         notificationService.createNotification(

                 property.getSeller().getId(),

                 user.getFullName()
                         + " commented on your property: "
                         + property.getTitle(),

                 "COMMENT",

                 property.getId(),

                 saved.getId()
         );
     }

        return convertToResponse(saved);
    }


    // =========================================
    // GET COMMENTS FOR PROPERTY
    // =========================================

    public List<PropertyCommentResponse>
    getPropertyComments(Long propertyId) {

        // Check that property exists
        propertyRepository.findById(propertyId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Property not found"
                        )
                );

        return commentRepository
                .findByPropertyIdOrderByCreatedAtDesc(
                        propertyId
                )
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }


    // =========================================
    // GET SELLER COMMENTS
    // =========================================

    public List<PropertyCommentResponse>
    getSellerComments(Long sellerId) {

        // Check that seller exists
        userRepository.findById(sellerId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Seller not found"
                        )
                );

        return commentRepository
                .findByPropertySellerIdOrderByCreatedAtDesc(
                        sellerId
                )
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }


    // =========================================
    // GET BUYER COMMENTS
    // =========================================

    public List<PropertyCommentResponse>
    getBuyerComments(Long userId) {

        // Check that user exists
        userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"
                        )
                );

        return commentRepository
                .findByUserIdOrderByCreatedAtDesc(
                        userId
                )
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }


    // =========================================
    // SELLER REPLY
    // =========================================

    public PropertyCommentResponse replyToComment(
            Long commentId,
            Long sellerId,
            String replyText) {

        if (replyText == null ||
                replyText.trim().isEmpty()) {

            throw new RuntimeException(
                    "Reply cannot be empty"
            );
        }

        PropertyComment comment =
                commentRepository.findById(commentId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Comment not found"
                                )
                        );

        User seller =
                userRepository.findById(sellerId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Seller not found"
                                )
                        );


        // =========================================
        // CHECK PROPERTY OWNERSHIP
        // =========================================

        if (comment.getProperty() == null ||
                comment.getProperty().getSeller() == null ||
                comment.getProperty()
                        .getSeller()
                        .getId() == null ||
                !comment.getProperty()
                        .getSeller()
                        .getId()
                        .equals(seller.getId())) {

            throw new RuntimeException(
                    "You are not the seller of this property"
            );
        }


        comment.setSellerReply(
                replyText.trim()
        );

        comment.setRepliedAt(
                LocalDateTime.now()
        );

        PropertyComment saved =
                commentRepository.save(comment);
        
     // =========================================
     // NOTIFY BUYER
     // =========================================

     if (comment.getUser() != null &&
             comment.getUser().getId() != null &&
             !comment.getUser()
                     .getId()
                     .equals(seller.getId())) {

         notificationService.createNotification(

                 comment.getUser().getId(),

                 seller.getFullName()
                         + " replied to your comment on: "
                         + comment.getProperty().getTitle(),

                 "REPLY",

                 comment.getProperty().getId(),

                 saved.getId()
         );
     }

        return convertToResponse(saved);
    }


    // =========================================
    // ENTITY → DTO
    // =========================================

    private PropertyCommentResponse convertToResponse(
            PropertyComment comment) {

        String propertyTitle = "";

        if (comment.getProperty() != null) {

            propertyTitle =
                    comment.getProperty().getTitle();
        }


        String userName = "User";

        if (comment.getUser() != null &&
                comment.getUser().getFullName() != null) {

            userName =
                    comment.getUser().getFullName();
        }


        return new PropertyCommentResponse(

                comment.getId(),

                comment.getProperty() != null
                        ? comment.getProperty().getId()
                        : null,

                propertyTitle,

                comment.getUser() != null
                        ? comment.getUser().getId()
                        : null,

                userName,

                comment.getComment(),

                comment.getCreatedAt(),

                comment.getSellerReply(),

                comment.getRepliedAt()
        );
    }
}