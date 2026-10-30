package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.SellerDashboardResponse;
import com.example.demo.entity.ListingType;
import com.example.demo.entity.Payment;
import com.example.demo.entity.PaymentStatus;
import com.example.demo.entity.Property;
import com.example.demo.entity.PropertyStatus;
import com.example.demo.entity.PropertyType;
import com.example.demo.entity.User;
import com.example.demo.entity.VisitStatus;
import com.example.demo.repository.ContactEnquiryRepository;
import com.example.demo.repository.PaymentRepository;
import com.example.demo.repository.PropertyCommentRepository;
import com.example.demo.repository.PropertyRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.VisitBookingRepository;

@Service
public class SellerDashboardService {

    private final PropertyRepository propertyRepository;
    private final VisitBookingRepository visitRepository;
    private final ContactEnquiryRepository enquiryRepository;
    private final UserRepository userRepository;
    private final PropertyCommentRepository commentRepository;
    private final PaymentRepository paymentRepository;

    public SellerDashboardService(
            PropertyRepository propertyRepository,
            VisitBookingRepository visitRepository,
            ContactEnquiryRepository enquiryRepository,
            UserRepository userRepository,
            PropertyCommentRepository commentRepository,
            PaymentRepository paymentRepository) {

        this.propertyRepository = propertyRepository;
        this.visitRepository = visitRepository;
        this.enquiryRepository = enquiryRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.paymentRepository = paymentRepository;
    }

    public SellerDashboardResponse getDashboard(Long sellerId) {

        User seller = userRepository.findById(sellerId)
                .orElseThrow(() ->
                        new RuntimeException("Seller not found"));

        // =========================================
        // EXISTING DASHBOARD DATA
        // =========================================

        long totalProperties =
                propertyRepository.countBySeller(seller);

        long pendingVisits =
                visitRepository.countByPropertySellerAndStatus(
                        seller,
                        VisitStatus.PENDING
                );

        long totalComments =
                commentRepository.countByPropertySellerId(sellerId);


        // =========================================
        // SELLER PROPERTIES
        // =========================================

        List<Property> properties =
                propertyRepository.findBySeller(seller);


        long availableProperties = 0;
        long bookedProperties = 0;
        long rentedProperties = 0;
        long soldProperties = 0;
        long saleListings = 0;
        long rentListings = 0;

        long apartmentCount = 0;
        long villaCount = 0;
        long independentHouseCount = 0;
        long plotCount = 0;
        long commercialCount = 0;
        


        // =========================================
        // PROPERTY ANALYTICS
        // =========================================

        for (Property property : properties) {

            // =========================
            // STATUS
            // =========================

            PropertyStatus status =
                    property.getStatus();

            if (status == PropertyStatus.AVAILABLE) {

                availableProperties++;

            } else if (status == PropertyStatus.BOOKED) {

                bookedProperties++;

        } else if (status == PropertyStatus.SOLD) {

            soldProperties++;

        } else if (status == PropertyStatus.RENTED) {

            rentedProperties++;
        }


            // =========================
            // LISTING TYPE
            // =========================

            ListingType listingType =
                    property.getListingType();

            if (listingType == ListingType.SALE) {

                saleListings++;

            } else if (listingType == ListingType.RENT) {

                rentListings++;
            }


            // =========================
            // PROPERTY TYPE
            // =========================

            PropertyType propertyType =
                    property.getPropertyType();

            if (propertyType == PropertyType.APARTMENT) {

                apartmentCount++;

            } else if (propertyType == PropertyType.VILLA) {

                villaCount++;

            } else if (
                    propertyType ==
                    PropertyType.HOUSE) {

                independentHouseCount++;

            } else if (propertyType == PropertyType.PLOT) {

                plotCount++;

            } else if (
                    propertyType ==
                    PropertyType.COMMERCIAL) {

                commercialCount++;
            }
        }


        // =========================================
        // ACTUAL SALES VALUE
        // =========================================
        //
        // Only SUCCESSFUL payments are counted.
        //
        // Example:
        // Property A -> ₹14 -> SUCCESS
        // Property B -> ₹40 -> SUCCESS
        //
        // Total Sales Value = ₹54
        //
        // PENDING / FAILED payments are NOT counted.
        // =========================================

        double totalSalesValue = 0;

        List<Payment> sellerPayments =
                paymentRepository.findByPropertySellerId(sellerId);

        for (Payment payment : sellerPayments) {

            if (payment.getStatus() == PaymentStatus.SUCCESS
                    && payment.getAmount() != null) {

                totalSalesValue += payment.getAmount();
            }
        }


        // =========================================
        // RESPONSE
        // =========================================

        return new SellerDashboardResponse(

                totalProperties,

                pendingVisits,

                totalComments,

                availableProperties,

                bookedProperties,

                rentedProperties,
              soldProperties,

                saleListings,

                rentListings,

                apartmentCount,

                villaCount,

                independentHouseCount,

                plotCount,

                commercialCount,

                totalSalesValue
        );
    }
}