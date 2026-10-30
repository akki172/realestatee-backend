package com.example.demo.service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.example.demo.dto.AIChatRequest;
import com.example.demo.dto.AIChatResponse;
import com.example.demo.dto.PropertyResponse;

@Service
public class AIService {

    private final PropertyService propertyService;

    public AIService(PropertyService propertyService) {
        this.propertyService = propertyService;
    }


    // =========================================
    // AI CHAT
    // =========================================

    public AIChatResponse chat(AIChatRequest request) {

        String message = request.getMessage();

        if (message == null || message.trim().isEmpty()) {

            return new AIChatResponse(
                    "Please tell me what property you are looking for."
            );
        }


        String text =
                message.toLowerCase().trim();


        // =========================================
        // PROPERTY TYPE
        // =========================================

        String propertyType =
                request.getPropertyType();


        if (propertyType == null) {

            if (text.contains("villa")) {

                propertyType = "VILLA";

            } else if (
                    text.contains("apartment")
                    || text.contains("flat")) {

                propertyType = "APARTMENT";

            } else if (text.contains("plot")) {

                propertyType = "PLOT";

            } else if (text.contains("commercial")) {

                propertyType = "COMMERCIAL";

            } else if (text.contains("house")) {

                propertyType = "HOUSE";
            }
        }


        // =========================================
        // ASK PROPERTY TYPE
        // =========================================

        if (propertyType == null) {

            return new AIChatResponse(

                    "What type of property are you looking for? "
                    + "For example: Villa, Apartment, House or Plot.",

                    null,

                    null,

                    request.getCity(),

                    request.getMaxPrice(),

                    null
            );
        }


        // =========================================
        // CITY
        // =========================================

        String city =
                request.getCity();


        if (text.contains("hyderabad")) {

            city = "Hyderabad";

        } else if (
                text.contains("bangalore")
                || text.contains("bengaluru")) {

            city = "Bangalore";

        } else if (text.contains("chennai")) {

            city = "Chennai";

        } else if (text.contains("vijayawada")) {

            city = "Vijayawada";

        } else if (
                text.contains("visakhapatnam")
                || text.contains("vizag")) {

            city = "Visakhapatnam";
        }


        // =========================================
        // ASK CITY
        // =========================================

        if (city == null) {

            return new AIChatResponse(

                    "Which city are you looking in?",

                    null,

                    propertyType,

                    null,

                    request.getMaxPrice(),

                    null
            );
        }


        // =========================================
        // BUDGET
        // =========================================

        Double maxPrice =
                request.getMaxPrice();


        if (maxPrice == null) {

            maxPrice =
                    extractBudget(text);
        }


        // =========================================
        // ASK BUDGET
        // =========================================

        if (maxPrice == null) {

            return new AIChatResponse(

                    "What's your approximate budget?",

                    null,

                    propertyType,

                    city,

                    null,

                    null
            );
        }


        // =========================================
        // SEARCH AVAILABLE PROPERTIES
        // =========================================

        List<PropertyResponse> properties =
                propertyService.searchAvailableProperties(

                        city,

                        null,

                        propertyType,

                        null,

                        maxPrice,

                        null
                );


        // =========================================
        // NO RESULTS
        // =========================================

        if (properties == null
                || properties.isEmpty()) {

            return new AIChatResponse(

                    "Sorry, I couldn't find any available "
                    + propertyType.toLowerCase()
                    + " properties in "
                    + city
                    + " within your budget.",

                    null,

                    propertyType,

                    city,

                    maxPrice,

                    null
            );
        }


        // =========================================
        // RESULTS
        // =========================================

        return new AIChatResponse(

                "Great! I found "
                + properties.size()
                + " available properties matching "
                + "your requirements.",

                properties,

                propertyType,

                city,

                maxPrice,

                null
        );
    }


    // =========================================
    // EXTRACT BUDGET
    // =========================================

    private Double extractBudget(String text) {

        // -----------------------------------------
        // CRORE
        // Examples:
        // 1 crore
        // 1 cr
        // 1.5 crore
        // -----------------------------------------

        Pattern crorePattern =
                Pattern.compile(
                        "(\\d+(?:\\.\\d+)?)\\s*(crore|crores|cr)",
                        Pattern.CASE_INSENSITIVE
                );

        Matcher croreMatcher =
                crorePattern.matcher(text);

        if (croreMatcher.find()) {

            double value =
                    Double.parseDouble(
                            croreMatcher.group(1)
                    );

            return value * 10000000;
        }


        // -----------------------------------------
        // LAKH
        // Examples:
        // 50 lakh
        // 50 lakhs
        // 70L
        // 70 l
        // -----------------------------------------

        Pattern lakhPattern =
                Pattern.compile(
                        "(\\d+(?:\\.\\d+)?)\\s*(lakh|lakhs|l)",
                        Pattern.CASE_INSENSITIVE
                );

        Matcher lakhMatcher =
                lakhPattern.matcher(text);

        if (lakhMatcher.find()) {

            double value =
                    Double.parseDouble(
                            lakhMatcher.group(1)
                    );

            return value * 100000;
        }


        // -----------------------------------------
        // RUPEES
        // Examples:
        // ₹10
        // ₹100
        // 10 rs
        // 10 rupees
        // Rs 10
        // -----------------------------------------

        Pattern rupeePattern =
                Pattern.compile(
                        "(?:₹|rs\\.?|rupees?)\\s*(\\d+(?:\\.\\d+)?)"
                        + "|(\\d+(?:\\.\\d+)?)\\s*(?:rs\\.?|rupees?)",
                        Pattern.CASE_INSENSITIVE
                );

        Matcher rupeeMatcher =
                rupeePattern.matcher(text);

        if (rupeeMatcher.find()) {

            String valueText =
                    rupeeMatcher.group(1) != null
                            ? rupeeMatcher.group(1)
                            : rupeeMatcher.group(2);

            return Double.parseDouble(valueText);
        }


        // -----------------------------------------
        // PLAIN NUMBER
        // Examples:
        // 10
        // 500
        // 1000
        // -----------------------------------------

        Pattern numberPattern =
                Pattern.compile(
                        "^\\d+(?:\\.\\d+)?$"
                );

        Matcher numberMatcher =
                numberPattern.matcher(text);

        if (numberMatcher.find()) {

            return Double.parseDouble(text);
        }


        return null;
    }

}