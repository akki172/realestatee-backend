package com.example.demo.dto;

import java.util.List;

public class AIChatResponse {

    private String message;

    private List<PropertyResponse> properties;

    private String propertyType;

    private String city;

    private Double maxPrice;

    private Integer bedrooms;


    public AIChatResponse() {
    }


    public AIChatResponse(String message) {
        this.message = message;
    }


    public AIChatResponse(
            String message,
            List<PropertyResponse> properties) {

        this.message = message;
        this.properties = properties;
    }


    public AIChatResponse(
            String message,
            List<PropertyResponse> properties,
            String propertyType,
            String city,
            Double maxPrice,
            Integer bedrooms) {

        this.message = message;
        this.properties = properties;
        this.propertyType = propertyType;
        this.city = city;
        this.maxPrice = maxPrice;
        this.bedrooms = bedrooms;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public List<PropertyResponse> getProperties() {
        return properties;
    }

    public void setProperties(
            List<PropertyResponse> properties) {

        this.properties = properties;
    }


    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }


    public Double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Double maxPrice) {
        this.maxPrice = maxPrice;
    }


    public Integer getBedrooms() {
        return bedrooms;
    }

    public void setBedrooms(Integer bedrooms) {
        this.bedrooms = bedrooms;
    }
}