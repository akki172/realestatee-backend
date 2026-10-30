package com.example.demo.dto;

import java.time.LocalDateTime;

import com.example.demo.entity.PaymentStatus;

public class TransactionReceiptResponse {

    private Long transactionId;

    private String orderId;

    private String paymentId;

    private String paymentSessionId;

    private PaymentStatus status;

    private Double amount;

    private LocalDateTime paymentDate;


    // ================================
    // BUYER DETAILS
    // ================================

    private String buyerName;

    private String buyerEmail;

    private String buyerPhone;


    // ================================
    // PROPERTY DETAILS
    // ================================

    private Long propertyId;

    private String propertyTitle;

    private String city;

    private String state;


    // ================================
    // GETTERS / SETTERS
    // ================================

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }


    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }


    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }


    public String getPaymentSessionId() {
        return paymentSessionId;
    }

    public void setPaymentSessionId(
            String paymentSessionId) {

        this.paymentSessionId =
                paymentSessionId;
    }


    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }


    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }


    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(
            LocalDateTime paymentDate) {

        this.paymentDate =
                paymentDate;
    }


    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }


    public String getBuyerEmail() {
        return buyerEmail;
    }

    public void setBuyerEmail(String buyerEmail) {
        this.buyerEmail = buyerEmail;
    }


    public String getBuyerPhone() {
        return buyerPhone;
    }

    public void setBuyerPhone(String buyerPhone) {
        this.buyerPhone = buyerPhone;
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

    public void setPropertyTitle(
            String propertyTitle) {

        this.propertyTitle =
                propertyTitle;
    }


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }


    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}