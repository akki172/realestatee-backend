package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.repository.PropertyRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.VisitBookingRepository;
import com.example.demo.dto.VisitBookingRequest;
import com.example.demo.entity.BuyerJourneyStatus;
import com.example.demo.entity.Property;
import com.example.demo.entity.User;
import com.example.demo.entity.VisitBooking;
import com.example.demo.entity.VisitStatus;

@Service
public class VisitBookingService {

    private final VisitBookingRepository visitBookingRepository;
    private final UserRepository userRepository;
    private final PropertyRepository propertyRepository;
    private final EmailService emailService;

    public VisitBookingService(
            VisitBookingRepository visitBookingRepository,
            UserRepository userRepository,
            PropertyRepository propertyRepository,
            EmailService emailService) {

        this.visitBookingRepository = visitBookingRepository;
        this.userRepository = userRepository;
        this.propertyRepository = propertyRepository;
        this.emailService=  emailService;
    }

    public String bookVisit(VisitBookingRequest request) {

        User buyer = userRepository.findById(request.getBuyerId())
                .orElseThrow(() -> new RuntimeException("Buyer not found"));

        Property property = propertyRepository.findById(request.getPropertyId())
                .orElseThrow(() -> new RuntimeException("Property not found"));

        VisitBooking booking = new VisitBooking();

        booking.setBuyer(buyer);
        booking.setProperty(property);
        booking.setVisitDate(request.getVisitDate());
        booking.setVisitTime(request.getVisitTime());
        booking.setMessage(request.getMessage());
        booking.setJourneyStatus(
        	    BuyerJourneyStatus.VISIT_REQUESTED
        	);
        booking.setStatus(VisitStatus.PENDING);

        visitBookingRepository.save(booking);

        // Send email to seller
        emailService.sendEmail(
                property.getSeller().getEmail(),
                "New Property Visit Request",
                "You have received a new visit request.\n\n"
                        + "Property: " + property.getTitle() + "\n"
                        + "Visit Date: " + request.getVisitDate() + "\n"
                        + "Visit Time: " + request.getVisitTime() + "\n"
                        + "Buyer: " + buyer.getFullName() + "\n"
                        + "Buyer Email: " + buyer.getEmail() + "\n"
                        + "Message: " + request.getMessage()
        );

        return "Visit request sent successfully.";
    }

    public List<VisitBooking> getVisits(Long buyerId) {

        User buyer = userRepository.findById(buyerId)
                .orElseThrow(() -> new RuntimeException("Buyer not found"));

        return visitBookingRepository.findByBuyer(buyer);
    }
    public List<VisitBooking> getSellerVisits(Long sellerId) {

        User seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new RuntimeException("Seller not found"));

        return visitBookingRepository.findByPropertySeller(seller);

    }
    public String approveVisit(Long id, String comment) {

        VisitBooking booking = visitBookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Visit not found"));

        booking.setStatus(VisitStatus.APPROVED);

        visitBookingRepository.save(booking);

        String emailMessage =
                "Your visit for " + booking.getProperty().getTitle()
                + " has been approved by the seller.\n\n"
                + "Visit Date: " + booking.getVisitDate() + "\n"
                + "Visit Time: " + booking.getVisitTime();

        if (comment != null && !comment.trim().isEmpty()) {
            emailMessage += "\n\nSeller Message:\n" + comment;
        }

        emailService.sendEmail(
                booking.getBuyer().getEmail(),
                "Visit Request Approved",
                emailMessage
        );
        booking.setJourneyStatus(
        	    BuyerJourneyStatus.VISIT_APPROVED
        	);

        return "Visit Approved";
    }
    public String rejectVisit(Long id, String reason) {

        if (reason == null || reason.trim().isEmpty()) {
            throw new RuntimeException("Rejection reason is required");
        }

        VisitBooking booking = visitBookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Visit not found"));

        booking.setStatus(VisitStatus.REJECTED);

        visitBookingRepository.save(booking);

        emailService.sendEmail(
                booking.getBuyer().getEmail(),
                "Visit Request Rejected",
                "Your visit for " + booking.getProperty().getTitle()
                + " has been rejected by the seller.\n\n"
                + "Reason:\n"
                + reason
        );
        
        return "Visit Rejected";
    }
    public String markAsVisited(Long id) {

        VisitBooking booking = visitBookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Visit not found"));

        if (booking.getStatus() != VisitStatus.APPROVED) {
            throw new RuntimeException(
                    "Visit must be approved before marking as visited"
            );
        }

        booking.setJourneyStatus(
                BuyerJourneyStatus.VISITED
        );

        visitBookingRepository.save(booking);

        return "Property visit marked as completed";
    }
    public String updateBuyerInterest(Long id, boolean interested) {

        VisitBooking booking = visitBookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Visit not found"));

        if (booking.getJourneyStatus() != BuyerJourneyStatus.VISITED) {
            throw new RuntimeException(
                    "Buyer must visit the property first"
            );
        }

        if (interested) {

            booking.setJourneyStatus(
                    BuyerJourneyStatus.INTERESTED
            );

            visitBookingRepository.save(booking);

            return "Buyer marked the property as interested";

        } else {

            booking.setJourneyStatus(
                    BuyerJourneyStatus.NOT_INTERESTED
            );

            visitBookingRepository.save(booking);

            return "Buyer marked the property as not interested";
        }
    }
}