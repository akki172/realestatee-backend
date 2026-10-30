package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.dto.AdminActivityResponse;
import com.example.demo.dto.AdminDashboardResponse;
import com.example.demo.dto.AdminLocationActivityResponse;
import com.example.demo.dto.AdminTransactionResponse;
import com.example.demo.dto.AdminUserResponse;
import com.example.demo.dto.PropertyResponse;
import com.example.demo.entity.ActivityType;
import com.example.demo.entity.Payment;
import com.example.demo.entity.PaymentStatus;
import com.example.demo.entity.Property;
import com.example.demo.entity.PropertyStatus;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.ContactEnquiryRepository;
import com.example.demo.repository.PaymentRepository;
import com.example.demo.repository.PropertyRepository;
import com.example.demo.repository.UserActivityRepository;
import com.example.demo.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminDashboardService {

    private final UserRepository userRepository;
    private final PropertyRepository propertyRepository;
    private final ContactEnquiryRepository enquiryRepository;
    private final PaymentRepository paymentRepository;
    private final UserActivityRepository
    userActivityRepository;
    
    public AdminDashboardService(
            UserRepository userRepository,
            PropertyRepository propertyRepository,
            ContactEnquiryRepository enquiryRepository,
            PaymentRepository paymentRepository,
     UserActivityRepository
        userActivityRepository){
        this.userRepository = userRepository;
        this.propertyRepository = propertyRepository;
        this.enquiryRepository = enquiryRepository;
        this.paymentRepository = paymentRepository;
        this.userActivityRepository=
        userActivityRepository;
    }


    // =========================================
    // ADMIN DASHBOARD
    // =========================================

    public AdminDashboardResponse getDashboard() {

        long totalUsers =
                userRepository.count();

        long totalBuyers =
                userRepository.countByRole(
                        Role.BUYER
                );

        long totalSellers =
                userRepository.countByRole(
                        Role.SELLER
                );


        long totalProperties =
                propertyRepository.count();

        long availableProperties =
                propertyRepository.countByStatus(
                        PropertyStatus.AVAILABLE
                );

        long bookedProperties =
                propertyRepository.countByStatus(
                        PropertyStatus.BOOKED
                );

        long soldProperties =
                propertyRepository.countByStatus(
                        PropertyStatus.SOLD
                );

        long rentedProperties =
                propertyRepository.countByStatus(
                        PropertyStatus.RENTED
                );


        long totalEnquiries =
                enquiryRepository.count();


        // =========================================
        // PAYMENT ANALYTICS
        // =========================================

        List<Payment> successfulPaymentsList =
                paymentRepository.findByStatus(
                        PaymentStatus.SUCCESS
                );

        long successfulPayments =
                successfulPaymentsList.size();

        double totalSalesValue = 0;

        for (Payment payment :
                successfulPaymentsList) {

            if (payment.getAmount() != null) {

                totalSalesValue +=
                        payment.getAmount();
            }
        }


        // =========================================
        // RESPONSE
        // =========================================

        return new AdminDashboardResponse(

                totalUsers,

                totalBuyers,

                totalSellers,

                totalProperties,

                availableProperties,

                bookedProperties,

                soldProperties,

                rentedProperties,

                totalEnquiries,

                successfulPayments,

                totalSalesValue
        );
    }


    // =========================================
    // GET ALL USERS
    // =========================================

    public List<AdminUserResponse> getAllUsers() {

        return userRepository.findAll()

                .stream()

                .map(user -> new AdminUserResponse(

                        user.getId(),

                        user.getFullName(),

                        user.getEmail(),

                        user.getPhone(),

                        user.getRole(),

                        user.getProfileImage(),

                        user.isVerified(),

                        user.isActive()

                )
                		)

                .collect(Collectors.toList());
    }


    // =========================================
    // GET USER BY ID
    // =========================================

    public AdminUserResponse getUserById(Long id) {

        User user =
                userRepository.findById(id)

                        .orElseThrow(
                                () -> new RuntimeException(
                                        "User not found with id: "
                                                + id
                                )
                        );


        return new AdminUserResponse(

                user.getId(),

                user.getFullName(),

                user.getEmail(),

                user.getPhone(),

                user.getRole(),

                user.getProfileImage(),

                user.isVerified(),

                user.isActive()

        );
    }


    // =========================================
    // ACTIVATE USER
    // =========================================

    public void activateUser(Long id) {

        User user =
                userRepository.findById(id)

                        .orElseThrow(
                                () -> new RuntimeException(
                                        "User not found with id: "
                                                + id
                                )
                        );


        user.setActive(true);

        userRepository.save(user);
    }


    // =========================================
    // DEACTIVATE USER
    // =========================================

    public void deactivateUser(Long id) {

        User user =
                userRepository.findById(id)

                        .orElseThrow(
                                () -> new RuntimeException(
                                        "User not found with id: "
                                                + id
                                )
                        );


        user.setActive(false);

        userRepository.save(user);
    }


    // =========================================
    // DELETE USER
    // =========================================

    public void deleteUser(Long id) {

        if (!userRepository.existsById(id)) {

            throw new RuntimeException(
                    "User not found with id: " + id
            );
        }


        userRepository.deleteById(id);
    }


    // =========================================
    // GET ALL PROPERTIES
    // =========================================

    public List<PropertyResponse> getAllProperties() {

        List<Property> properties =
                propertyRepository.findAll();


        List<PropertyResponse> responseList =
                new ArrayList<>();


        for (Property property : properties) {

            PropertyResponse response =
                    new PropertyResponse();


            response.setId(
                    property.getId()
            );


            response.setTitle(
                    property.getTitle()
            );


            response.setPrice(
                    property.getPrice()
            );


            response.setCity(
                    property.getCity()
            );


            response.setState(
                    property.getState()
            );


            response.setPropertyType(
                    property.getPropertyType() != null

                            ? property
                                .getPropertyType()
                                .name()

                            : null
            );


            response.setListingType(
                    property.getListingType() != null

                            ? property
                                .getListingType()
                                .name()

                            : null
            );


            response.setStatus(
                    property.getStatus() != null

                            ? property
                                .getStatus()
                                .name()

                            : null
            );
            response.setVerified(
                    property.getVerified() != null
                            && property.getVerified()
            );


            response.setImageUrls(
                    property.getImageUrls()
            );


            if (property.getSeller() != null) {

                response.setSellerName(
                        property
                            .getSeller()
                            .getFullName()
                );
            }


            responseList.add(response);
        }


        return responseList;
    }


    // =========================================
    // DELETE PROPERTY
    // =========================================

    public void deleteProperty(Long id) {

        if (!propertyRepository.existsById(id)) {

            throw new RuntimeException(
                    "Property not found with id: " + id
            );
        }


        propertyRepository.deleteById(id);
    }
 // =========================================
 // VERIFY PROPERTY
 // =========================================

 public void verifyProperty(Long id) {

     Property property =
             propertyRepository.findById(id)
                     .orElseThrow(
                             () -> new RuntimeException(
                                     "Property not found with id: " + id
                             )
                     );

     property.setVerified(true);

     propertyRepository.save(property);
 }


 // =========================================
 // UNVERIFY PROPERTY
 // =========================================

 public void unverifyProperty(Long id) {

     Property property =
             propertyRepository.findById(id)
                     .orElseThrow(
                             () -> new RuntimeException(
                                     "Property not found with id: " + id
                             )
                     );

     property.setVerified(false);

     propertyRepository.save(property);
 }
 public List<AdminTransactionResponse> getAllTransactions() {

	    List<Payment> payments =
	            paymentRepository.findAll();

	    List<AdminTransactionResponse> responseList =
	            new ArrayList<>();

	    for (Payment payment : payments) {

	        Property property =
	                payment.getProperty();

	        User buyer =
	                payment.getBuyer();

	        String sellerName = null;

	        if (property != null &&
	            property.getSeller() != null) {

	            sellerName =
	                    property.getSeller()
	                            .getFullName();
	        }


	        String buyerName = null;

	        if (buyer != null) {

	            buyerName =
	                    buyer.getFullName();
	        }


	        String propertyTitle = null;

	        String propertyImage = null;

	        String propertyStatus = null;


	        if (property != null) {

	            propertyTitle =
	                    property.getTitle();

	            propertyStatus =
	                    property.getStatus() != null
	                            ? property.getStatus().name()
	                            : null;


	            if (property.getImageUrls() != null &&
	                !property.getImageUrls().isEmpty()) {

	                propertyImage =
	                        property.getImageUrls().get(0);
	            }
	        }


	        AdminTransactionResponse response =
	                new AdminTransactionResponse(

	                        payment.getId(),

	                        propertyTitle,

	                        propertyImage,

	                        sellerName,

	                        buyerName,

	                        payment.getAmount(),

	                        payment.getPaymentId(),

	                        payment.getOrderId(),

	                        payment.getStatus() != null
	                                ? payment.getStatus().name()
	                                : null,

	                        propertyStatus,

	                        payment.getCreatedAt()
	                );


	        responseList.add(response);
	    }


	    return responseList;
	}
 public List<AdminActivityResponse>
 getActivityAnalytics() {

List<AdminActivityResponse> response =
     new ArrayList<>();


for (ActivityType type :
     ActivityType.values()) {

 long count =
         userActivityRepository
                 .countByActivityType(type);


 response.add(
         new AdminActivityResponse(
                 type.name(),
                 count
         )
 );
}


return response;
}
 public List<AdminLocationActivityResponse>
 getLocationAnalytics() {

List<Object[]> results =
     userActivityRepository
             .getActivityByLocation();


List<AdminLocationActivityResponse>
     response = new ArrayList<>();


for (Object[] result : results) {

 String location =
         (String) result[0];

 long count =
         ((Number) result[1]).longValue();


 response.add(
         new AdminLocationActivityResponse(
                 location,
                 count
         )
 );
}


return response;
}
}