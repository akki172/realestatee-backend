package com.example.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.PropertyRequest;
import com.example.demo.dto.PropertyResponse;
import com.example.demo.service.PropertyService;
import com.example.demo.service.UserActivityService;

import java.util.List;
import com.example.demo.entity.*;
import com.example.demo.repository.UserRepository;

@RestController
@RequestMapping("/api/property")
@CrossOrigin(origins = "http://localhost:5173")
public class PropertyController {

   
    private final PropertyService propertyService;
    private final UserActivityService userActivityService;
    private final UserRepository userRepository;

    public PropertyController(
            PropertyService propertyService,
            UserActivityService userActivityService,
            UserRepository userRepository) {

        this.propertyService = propertyService;
        this.userActivityService = userActivityService;
        this.userRepository = userRepository;
    }
    @PostMapping("/add")
    public ResponseEntity<String> addProperty(
            @RequestBody PropertyRequest request) {

        return ResponseEntity.ok(
                propertyService.addProperty(request)
        );
    }
    @GetMapping("/all")
    public ResponseEntity<List<PropertyResponse>> getAllProperties() {

        return ResponseEntity.ok(
                propertyService.getAllProperties()
        );
    }
    @GetMapping("/{id}")
    public ResponseEntity<PropertyResponse> getPropertyById(
            @PathVariable Long id,
            Authentication authentication) {

        PropertyResponse property =
                propertyService.getPropertyById(id);


        // =========================================
        // RECORD PROPERTY VIEW
        // =========================================

        if (authentication != null) {

            User user =
                    userRepository.findByEmail(
                            authentication.getName()
                    ).orElse(null);

            if (user != null) {

                userActivityService.recordActivity(

                        user.getId(),

                        id,

                        ActivityType.PROPERTY_VIEW,

                        property.getCity()
                );
            }
        }


        return ResponseEntity.ok(property);
    }
    @GetMapping("/city/{city}")
    public ResponseEntity<List<Property>> searchByCity(
            @PathVariable String city) {

        return ResponseEntity.ok(
                propertyService.searchByCity(city)
        );

    }
    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<PropertyResponse>> getSellerProperties(
            @PathVariable Long sellerId) {

        return ResponseEntity.ok(
                propertyService.getSellerPropertiesResponse(sellerId)
        );
    }
    @PutMapping("/{id}")
    public ResponseEntity<String> updateProperty(
            @PathVariable Long id,
            @RequestBody PropertyRequest request) {

        return ResponseEntity.ok(
                propertyService.updateProperty(id, request)
        );
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProperty(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                propertyService.deleteProperty(id)
        );
    }
    @PutMapping("/{id}/status")
    public ResponseEntity<String> updatePropertyStatus(
            @PathVariable Long id,
            @RequestParam PropertyStatus status,
            Authentication authentication) {

        if (authentication == null) {
            return ResponseEntity
                    .status(401)
                    .body("Authentication required");
        }

        String sellerEmail =
                authentication.getName();

        return ResponseEntity.ok(
                propertyService.updatePropertyStatus(
                        id,
                        status,
                        sellerEmail
                )
        );
    }
    @GetMapping("/nearby")
    public ResponseEntity<List<PropertyResponse>> getNearbyProperties(
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam(defaultValue = "10") Double radiusKm) {

        return ResponseEntity.ok(
                propertyService.getNearbyProperties(
                        latitude,
                        longitude,
                        radiusKm
                )
        );
    }
    @GetMapping("/search")
    public ResponseEntity<List<PropertyResponse>> searchProperties(

            @RequestParam(required = false)
            String city,

            @RequestParam(required = false)
            String listingType,

            @RequestParam(required = false)
            String propertyType,

            @RequestParam(required = false)
            Double minPrice,

            @RequestParam(required = false)
            Double maxPrice,

            @RequestParam(required = false)
            Integer bedrooms,

            Authentication authentication) {


        // =========================================
        // RECORD SEARCH ACTIVITY
        // =========================================

        if (authentication != null) {

            User user =
                    userRepository.findByEmail(
                            authentication.getName()
                    ).orElse(null);


            if (user != null) {

                userActivityService.recordActivity(

                        user.getId(),

                        null,

                        ActivityType.PROPERTY_SEARCH,

                        city
                );
            }
        }


        // =========================================
        // PERFORM SEARCH
        // =========================================

        return ResponseEntity.ok(

                propertyService.searchProperties(

                        city,
                        listingType,
                        propertyType,
                        minPrice,
                        maxPrice,
                        bedrooms

                )
        );
    }
    
}