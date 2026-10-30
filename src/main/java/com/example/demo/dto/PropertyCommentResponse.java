package com.example.demo.dto;

import java.time.LocalDateTime;

public class PropertyCommentResponse {

    private Long id;

    private Long propertyId;

    private String propertyTitle;

    private Long userId;

    private String userName;

    private String comment;

    private LocalDateTime createdAt;

    private String sellerReply;

    private LocalDateTime repliedAt;


    public PropertyCommentResponse() {
    }


    public PropertyCommentResponse(
            Long id,
            Long propertyId,
            String propertyTitle,
            Long userId,
            String userName,
            String comment,
            LocalDateTime createdAt,
            String sellerReply,
            LocalDateTime repliedAt) {

        this.id = id;
        this.propertyId = propertyId;
        this.propertyTitle = propertyTitle;
        this.userId = userId;
        this.userName = userName;
        this.comment = comment;
        this.createdAt = createdAt;
        this.sellerReply = sellerReply;
        this.repliedAt = repliedAt;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Long propertyId) {
        this.propertyId = propertyId;
    }


    public String getPropertyTitle() {
        return propertyTitle;
    }

    public void setPropertyTitle(String propertyTitle) {
        this.propertyTitle = propertyTitle;
    }


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }


    public String getSellerReply() {
        return sellerReply;
    }

    public void setSellerReply(String sellerReply) {
        this.sellerReply = sellerReply;
    }


    public LocalDateTime getRepliedAt() {
        return repliedAt;
    }

    public void setRepliedAt(LocalDateTime repliedAt) {
        this.repliedAt = repliedAt;
    }
}