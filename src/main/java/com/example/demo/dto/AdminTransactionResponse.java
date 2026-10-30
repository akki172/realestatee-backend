package com.example.demo.dto;

import java.time.LocalDateTime;

public class AdminTransactionResponse {

    private Long id;

    private String propertyTitle;

    private String propertyImage;

    private String sellerName;

    private String buyerName;

    private Double amount;

    private String paymentId;

    private String orderId;

    private String paymentStatus;

    private String propertyStatus;

    private LocalDateTime createdAt;


    public AdminTransactionResponse() {
    }


    public AdminTransactionResponse(
            Long id,
            String propertyTitle,
            String propertyImage,
            String sellerName,
            String buyerName,
            Double amount,
            String paymentId,
            String orderId,
            String paymentStatus,
            String propertyStatus,
            LocalDateTime createdAt) {

        this.id = id;
        this.propertyTitle = propertyTitle;
        this.propertyImage = propertyImage;
        this.sellerName = sellerName;
        this.buyerName = buyerName;
        this.amount = amount;
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.paymentStatus = paymentStatus;
        this.propertyStatus = propertyStatus;
        this.createdAt = createdAt;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getPropertyTitle() {
        return propertyTitle;
    }

    public void setPropertyTitle(String propertyTitle) {
        this.propertyTitle = propertyTitle;
    }


    public String getPropertyImage() {
        return propertyImage;
    }

    public void setPropertyImage(String propertyImage) {
        this.propertyImage = propertyImage;
    }


    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }


    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }


    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }


    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }


    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }


    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }


    public String getPropertyStatus() {
        return propertyStatus;
    }

    public void setPropertyStatus(String propertyStatus) {
        this.propertyStatus = propertyStatus;
    }


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}