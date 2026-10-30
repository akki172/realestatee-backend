package com.example.demo.service;

import java.util.ArrayList;
import com.example.demo.entity.ListingType;
import com.example.demo.entity.Payment;
import com.example.demo.entity.PaymentStatus;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.PropertyRequest;
import com.example.demo.dto.PropertyResponse;
import com.example.demo.entity.Property;
import com.example.demo.entity.PropertyStatus;
import com.example.demo.entity.PropertyType;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.PaymentRepository;
import com.example.demo.repository.PropertyRepository;
import com.example.demo.repository.UserRepository;


@Service
public class PropertyService {

    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final PaymentRepository paymentRepository;

    public PropertyService(
            PropertyRepository propertyRepository,
            UserRepository userRepository,
            NotificationService notificationService,
            PaymentRepository paymentRepository
    ) {
        this.propertyRepository = propertyRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
        this.paymentRepository = paymentRepository;
    }

    public String addProperty(PropertyRequest request) {

        User seller = userRepository.findById(request.getSellerId())
                .orElseThrow(() -> new RuntimeException("Seller not found"));

        Property property = new Property();

        property.setTitle(request.getTitle());
        property.setDescription(request.getDescription());

        property.setPropertyType(
                PropertyType.valueOf(request.getPropertyType().toUpperCase())
        );

        property.setPrice(request.getPrice());
        property.setArea(request.getArea());

        property.setBedrooms(request.getBedrooms());
        property.setBathrooms(request.getBathrooms());
        property.setBalconies(request.getBalconies());
        property.setParking(request.getParking());

        property.setFurnished(request.getFurnished());

        property.setFloor(request.getFloor());
        property.setTotalFloors(request.getTotalFloors());
        property.setAgeOfProperty(request.getAgeOfProperty());

        property.setAddress(request.getAddress());
        property.setCity(request.getCity());
        property.setState(request.getState());
        property.setPincode(request.getPincode());

        property.setLatitude(request.getLatitude());
        property.setLongitude(request.getLongitude());
        property.setListingType(
        	    ListingType.valueOf(
        	        request.getListingType().toUpperCase()
        	    )
        	);
        property.setImageUrls(request.getImageUrls());

        property.setSeller(seller);

        propertyRepository.save(property);
    

      

List<User> buyers =
        userRepository.findByRole(Role.BUYER);

for (User buyer : buyers) {

    notificationService.createNotification(
            buyer.getId(),
            "🏠 New Property Added! \""
                    + property.getTitle()
                    + "\" is now available. Please visit and explore this property.",
            "NEW_PROPERTY",
            property.getId(),
            null
    );
}

return "Property Added Successfully";
    }
    public List<PropertyResponse> getAllProperties() {

        List<Property> properties =
                propertyRepository.findAll();

        List<PropertyResponse> responseList =
                new ArrayList<>();

        for (Property property : properties) {

            PropertyResponse response =
                    new PropertyResponse();

            response.setId(property.getId());

            response.setTitle(
                    property.getTitle()
            );

            response.setDescription(
                    property.getDescription()
            );

            response.setPropertyType(
                    property.getPropertyType().name()
            );

            response.setPrice(
                    property.getPrice()
            );

            response.setArea(
                    property.getArea()
            );

            response.setBedrooms(
                    property.getBedrooms()
            );

            response.setBathrooms(
                    property.getBathrooms()
            );

            response.setBalconies(
                    property.getBalconies()
            );

            response.setParking(
                    property.getParking()
            );

            response.setFurnished(
                    property.getFurnished()
            );

            response.setAddress(
                    property.getAddress()
            );

            response.setCity(
                    property.getCity()
            );

            response.setState(
                    property.getState()
            );

            response.setPincode(
                    property.getPincode()
            );

            response.setImageUrls(
                    property.getImageUrls()
            );


            // =========================================
            // PROPERTY STATUS
            // =========================================

            response.setStatus(
                    property.getStatus() != null
                            ? property.getStatus().name()
                            : "AVAILABLE"
            );


            // =========================================
            // VERIFIED
            // =========================================

            response.setVerified(
                    property.getVerified() != null
                            && property.getVerified()
            );


            // =========================================
            // SELLER INFORMATION
            // =========================================

            if (property.getSeller() != null) {

                response.setSellerId(
                        property.getSeller().getId()
                );

                response.setSellerName(
                        property.getSeller().getFullName()
                );
            }


            responseList.add(response);
        }

        return responseList;
    }
    public PropertyResponse getPropertyById(Long id) {

        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Property not found"));

        PropertyResponse response = new PropertyResponse();

        response.setId(property.getId());
        response.setTitle(property.getTitle());
        response.setDescription(property.getDescription());
        response.setPropertyType(property.getPropertyType().name());
        response.setPrice(property.getPrice());
        response.setArea(property.getArea());
        response.setBedrooms(property.getBedrooms());
        response.setBathrooms(property.getBathrooms());
        response.setBalconies(property.getBalconies());
        response.setParking(property.getParking());
        response.setFurnished(property.getFurnished());
        response.setAddress(property.getAddress());
        response.setCity(property.getCity());
        response.setState(property.getState());
        response.setPincode(property.getPincode());
        response.setImageUrls(property.getImageUrls());
        response.setFloor(property.getFloor());
        response.setTotalFloors(property.getTotalFloors());
        response.setAgeOfProperty(property.getAgeOfProperty());

        response.setLatitude(property.getLatitude());
        response.setLongitude(property.getLongitude());
        response.setStatus(
        	    property.getStatus() != null
        	        ? property.getStatus().name()
        	        : "AVAILABLE"
        	);
        response.setListingType(
        	    property.getListingType() != null
        	        ? property.getListingType().name()
        	        : null
        	);
        response.setVerified(
                property.getVerified() != null
                        && property.getVerified()
        );

        if (property.getSeller() != null) {

            response.setSellerId(
                property.getSeller().getId()
            );

            response.setSellerName(
                property.getSeller().getFullName()
            );
        }

        return response;
    }
    public List<Property> searchByType(String type) {

        return propertyRepository.findByPropertyType(
                PropertyType.valueOf(type.toUpperCase())
        );

    }
    public List<Property> searchByBudget(Double minPrice, Double maxPrice) {

        return propertyRepository.findByPriceBetween(
                minPrice,
                maxPrice
        );

    }
    public List<Property> searchByCity(String city) {

        return propertyRepository.findByCityIgnoreCase(city);

    }
    public List<Property> getSellerProperties(Long sellerId) {

        User seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new RuntimeException("Seller not found"));

        return propertyRepository.findBySeller(seller);

    }
    public String updateProperty(Long id, PropertyRequest request) {

        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Property not found"));

        property.setTitle(request.getTitle());
        property.setDescription(request.getDescription());
        property.setPrice(request.getPrice());
        property.setArea(request.getArea());
        property.setBedrooms(request.getBedrooms());
        property.setBathrooms(request.getBathrooms());
        property.setBalconies(request.getBalconies());
        property.setParking(request.getParking());
        property.setFurnished(request.getFurnished());
        property.setAddress(request.getAddress());
        property.setCity(request.getCity());
        property.setState(request.getState());
        property.setPincode(request.getPincode());
        property.setImageUrls(request.getImageUrls());
        property.setListingType(
        	    ListingType.valueOf(
        	        request.getListingType().toUpperCase()
        	    )
        	);
        

        propertyRepository.save(property);

        return "Property Updated Successfully";
    }
    public String deleteProperty(Long id) {

        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Property not found"));

        propertyRepository.delete(property);

        return "Property Deleted Successfully";
    }
    public String updatePropertyStatus(
            Long id,
            PropertyStatus status,
            String sellerEmail) {

        Property property =
                propertyRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Property not found"
                                )
                        );

        // =========================================
        // FIND LOGGED-IN SELLER
        // =========================================

        User seller =
                userRepository.findByEmail(sellerEmail)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Seller not found"
                                )
                        );


        // =========================================
        // VERIFY PROPERTY OWNER
        // =========================================

        if (property.getSeller() == null ||
            !property.getSeller()
                    .getId()
                    .equals(seller.getId())) {

            throw new RuntimeException(
                    "You are not authorized to update this property"
            );
        }


        // =========================================
        // ONLY BOOKED PROPERTY CAN BECOME SOLD
        // =========================================

        if (property.getStatus() != PropertyStatus.BOOKED) {

            throw new RuntimeException(
                    "Only a booked property can be marked as sold"
            );
        }


        // =========================================
        // ONLY SOLD STATUS ALLOWED HERE
        // =========================================

        if (status != PropertyStatus.SOLD) {

            throw new RuntimeException(
                    "Invalid status change"
            );
        }


        // =========================================
        // BOOKED → SOLD
        // =========================================

        property.setStatus(PropertyStatus.SOLD);

        propertyRepository.save(property);


        // =========================================
        // FIND SUCCESSFUL PAYMENT
        // =========================================

        List<Payment> payments =
                paymentRepository.findByPropertyId(
                        property.getId()
                );

        Payment successfulPayment =
                payments.stream()
                        .filter(payment ->
                                payment.getStatus()
                                        == PaymentStatus.SUCCESS
                        )
                        .findFirst()
                        .orElse(null);


        // =========================================
        // SEND SOLD NOTIFICATIONS
        // =========================================

        if (successfulPayment != null) {

            User buyer =
                    successfulPayment.getBuyer();


            // =====================================
            // SELLER NOTIFICATION
            // =====================================

            notificationService.createNotification(

                    seller.getId(),

                    "🎉 Congratulations! Your property \""
                            + property.getTitle()
                            + "\" has been successfully sold to "
                            + buyer.getFullName()
                            + ".",

                    "PROPERTY_SOLD",

                    property.getId(),

                    null
            );


            // =====================================
            // BUYER NOTIFICATION
            // =====================================

            notificationService.createNotification(

                    buyer.getId(),

                    "🎉 Congratulations on buying \""
                            + property.getTitle()
                            + "\"! "
                            + "Your property purchase has been successfully completed.",

                    "PROPERTY_PURCHASED",

                    property.getId(),

                    null
            );
        }


        return "Property marked as SOLD successfully";
    }
    public List<PropertyResponse> getNearbyProperties(
            Double latitude,
            Double longitude,
            Double radiusKm) {

        List<Property> properties = propertyRepository.findAll();

        List<PropertyResponse> nearbyProperties = new ArrayList<>();

        for (Property property : properties) {

            // Only show available properties
            if (property.getStatus() != null &&
                    property.getStatus() != PropertyStatus.AVAILABLE) {
                continue;
            }

            // Ignore properties without coordinates
            if (property.getLatitude() == null ||
                    property.getLongitude() == null) {
                continue;
            }

            double distance = calculateDistance(
                    latitude,
                    longitude,
                    property.getLatitude(),
                    property.getLongitude()
            );

            if (distance <= radiusKm) {

                PropertyResponse response =
                        new PropertyResponse();

                response.setId(property.getId());
                response.setTitle(property.getTitle());
                response.setDescription(property.getDescription());
                response.setPropertyType(
                        property.getPropertyType().name()
                );
                response.setPrice(property.getPrice());

                response.setArea(property.getArea());
                response.setBedrooms(property.getBedrooms());
                response.setBathrooms(property.getBathrooms());

                response.setAddress(property.getAddress());
                response.setCity(property.getCity());
                response.setState(property.getState());
                response.setPincode(property.getPincode());

                response.setLatitude(
                        property.getLatitude()
                );

                response.setLongitude(
                        property.getLongitude()
                );

                response.setStatus(
                        property.getStatus() != null
                                ? property.getStatus().name()
                                : "AVAILABLE"
                );

                response.setListingType(
                        property.getListingType() != null
                                ? property.getListingType().name()
                                : null
                );

                response.setImageUrls(
                        property.getImageUrls()
                );
                response.setVerified(
                        property.getVerified() != null
                                && property.getVerified()
                );

                if (property.getSeller() != null) {
                    response.setSellerName(
                            property.getSeller().getFullName()
                    );
                }

                nearbyProperties.add(response);
            }
        }

        return nearbyProperties;
    }
    private double calculateDistance(
            double lat1,
            double lon1,
            double lat2,
            double lon2) {

        final int EARTH_RADIUS_KM = 6371;

        double latDistance =
                Math.toRadians(lat2 - lat1);

        double lonDistance =
                Math.toRadians(lon2 - lon1);

        double a =
                Math.sin(latDistance / 2)
                        * Math.sin(latDistance / 2)
                +
                Math.cos(Math.toRadians(lat1))
                        * Math.cos(Math.toRadians(lat2))
                        * Math.sin(lonDistance / 2)
                        * Math.sin(lonDistance / 2);

        double c =
                2 * Math.atan2(
                        Math.sqrt(a),
                        Math.sqrt(1 - a)
                );

        return EARTH_RADIUS_KM * c;
    }
    
    public List<PropertyResponse> searchProperties(
            String city,
            String listingType,
            String propertyType,
            Double minPrice,
            Double maxPrice,
            Integer bedrooms) {

        ListingType listingTypeEnum = null;
        PropertyType propertyTypeEnum = null;

        if (listingType != null && !listingType.isBlank()) {
            listingTypeEnum =
                    ListingType.valueOf(
                            listingType.toUpperCase()
                    );
        }

        if (propertyType != null && !propertyType.isBlank()) {
            propertyTypeEnum =
                    PropertyType.valueOf(
                            propertyType.toUpperCase()
                    );
        }

        List<Property> properties =
                propertyRepository.searchProperties(
                        city,
                        listingTypeEnum,
                        propertyTypeEnum,
                        minPrice,
                        maxPrice,
                        bedrooms
                );

        List<PropertyResponse> responseList =
                new ArrayList<>();

        for (Property property : properties) {

            PropertyResponse response =
                    new PropertyResponse();

            response.setId(property.getId());
            response.setTitle(property.getTitle());
            response.setDescription(property.getDescription());

            if (property.getPropertyType() != null) {
                response.setPropertyType(
                        property.getPropertyType().name()
                );
            }

            response.setPrice(property.getPrice());
            response.setArea(property.getArea());
            response.setBedrooms(property.getBedrooms());
            response.setBathrooms(property.getBathrooms());
            response.setBalconies(property.getBalconies());
            response.setParking(property.getParking());
            response.setFurnished(property.getFurnished());

            response.setAddress(property.getAddress());
            response.setCity(property.getCity());
            response.setState(property.getState());
            response.setPincode(property.getPincode());

            response.setImageUrls(
                    property.getImageUrls()
            );

            response.setLatitude(
                    property.getLatitude()
            );

            response.setLongitude(
                    property.getLongitude()
            );

            response.setFloor(
                    property.getFloor()
            );

            response.setTotalFloors(
                    property.getTotalFloors()
            );

            response.setAgeOfProperty(
                    property.getAgeOfProperty()
            );

            response.setStatus(
                    property.getStatus() != null
                            ? property.getStatus().name()
                            : "AVAILABLE"
            );

            response.setListingType(
                    property.getListingType() != null
                            ? property.getListingType().name()
                            : null
            );
            response.setVerified(
                    property.getVerified() != null
                            && property.getVerified()
            );

            if (property.getSeller() != null) {
                response.setSellerName(
                        property.getSeller().getFullName()
                );
            }

            responseList.add(response);
        }

        return responseList;
    }
    public List<PropertyResponse> getSellerPropertiesResponse(Long sellerId) {

        User seller = userRepository.findById(sellerId)
                .orElseThrow(() ->
                        new RuntimeException("Seller not found")
                );

        List<Property> properties =
                propertyRepository.findBySeller(seller);

        return properties.stream()
                .map(property -> {

                    PropertyResponse response =
                            new PropertyResponse();

                    response.setId(property.getId());
                    response.setTitle(property.getTitle());
                    response.setDescription(property.getDescription());
                    response.setPrice(property.getPrice());
                    response.setArea(property.getArea());
                    response.setBedrooms(property.getBedrooms());
                    response.setBathrooms(property.getBathrooms());
                    response.setBalconies(property.getBalconies());
                    response.setParking(property.getParking());
                    response.setFurnished(property.getFurnished());
                    response.setFloor(property.getFloor());
                    response.setTotalFloors(property.getTotalFloors());
                    response.setAgeOfProperty(
                            property.getAgeOfProperty()
                    );

                    response.setAddress(property.getAddress());
                    response.setCity(property.getCity());
                    response.setState(property.getState());
                    response.setPincode(property.getPincode());

                    response.setLatitude(property.getLatitude());
                    response.setLongitude(property.getLongitude());

                    response.setStatus(
                            property.getStatus() != null
                                    ? property.getStatus().name()
                                    : null
                    );

                    response.setListingType(
                            property.getListingType() != null
                                    ? property.getListingType().name()
                                    : null
                    );

                    response.setImageUrls(
                            property.getImageUrls()
                    );
                    response.setVerified(
                            property.getVerified() != null
                                    && property.getVerified()
                    );

                    // SELLER INFORMATION
                    if (property.getSeller() != null) {

                        response.setSellerId(
                                property.getSeller().getId()
                        );

                        response.setSellerName(
                                property.getSeller().getFullName()
                        );
                    }

                    return response;

                })
                .toList();
    }
    public List<PropertyResponse> searchAvailableProperties(
            String city,
            String listingType,
            String propertyType,
            Double minPrice,
            Double maxPrice,
            Integer bedrooms) {

        ListingType listingTypeEnum = null;
        PropertyType propertyTypeEnum = null;

        if (listingType != null && !listingType.isBlank()) {

            listingTypeEnum =
                    ListingType.valueOf(
                            listingType.toUpperCase()
                    );
        }

        if (propertyType != null && !propertyType.isBlank()) {

            propertyTypeEnum =
                    PropertyType.valueOf(
                            propertyType.toUpperCase()
                    );
        }

        List<Property> properties =
                propertyRepository.searchAvailableProperties(
                        city,
                        listingTypeEnum,
                        propertyTypeEnum,
                        minPrice,
                        maxPrice,
                        bedrooms
                );

        List<PropertyResponse> responseList =
                new ArrayList<>();

        // Use the same Property → PropertyResponse
        // mapping you already have in searchProperties().

        for (Property property : properties) {

            PropertyResponse response =
                    new PropertyResponse();

            response.setId(property.getId());
            response.setTitle(property.getTitle());
            response.setDescription(property.getDescription());
            response.setPropertyType(
                    property.getPropertyType() != null
                            ? property.getPropertyType().name()
                            : null
            );
            response.setPrice(property.getPrice());
            response.setArea(property.getArea());
            response.setBedrooms(property.getBedrooms());
            response.setBathrooms(property.getBathrooms());
            response.setBalconies(property.getBalconies());
            response.setParking(property.getParking());
            response.setFurnished(property.getFurnished());
            response.setAddress(property.getAddress());
            response.setCity(property.getCity());
            response.setState(property.getState());
            response.setPincode(property.getPincode());
            response.setImageUrls(property.getImageUrls());
            response.setLatitude(property.getLatitude());
            response.setLongitude(property.getLongitude());
            response.setFloor(property.getFloor());
            response.setTotalFloors(property.getTotalFloors());
            response.setAgeOfProperty(property.getAgeOfProperty());

            response.setStatus(
                    PropertyStatus.AVAILABLE.name()
            );

            response.setListingType(
                    property.getListingType() != null
                            ? property.getListingType().name()
                            : null
            );

            response.setVerified(
                    property.getVerified() != null
                            && property.getVerified()
            );

            if (property.getSeller() != null) {

                response.setSellerName(
                        property.getSeller().getFullName()
                );
            }

            responseList.add(response);
        }

        return responseList;
    }
}