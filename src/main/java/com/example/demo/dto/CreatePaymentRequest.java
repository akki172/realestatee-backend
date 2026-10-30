package com.example.demo.dto;

public class CreatePaymentRequest {

    private Long propertyId;

    public CreatePaymentRequest() {
    }

    public Long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Long propertyId) {
        this.propertyId = propertyId;
    }
}