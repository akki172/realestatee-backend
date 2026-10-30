package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.repository.ContactEnquiryRepository;
import com.example.demo.repository.PropertyRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.dto.ContactRequest;
import com.example.demo.entity.ContactEnquiry;
import com.example.demo.entity.Property;
import com.example.demo.entity.User;

@Service
public class ContactService {

    private final ContactEnquiryRepository contactRepository;
    private final UserRepository userRepository;
    private final PropertyRepository propertyRepository;

    public ContactService(ContactEnquiryRepository contactRepository,
                          UserRepository userRepository,
                          PropertyRepository propertyRepository) {

        this.contactRepository = contactRepository;
        this.userRepository = userRepository;
        this.propertyRepository = propertyRepository;
    }

    public String sendEnquiry(ContactRequest request) {

        User buyer = userRepository.findById(request.getBuyerId())
                .orElseThrow(() -> new RuntimeException("Buyer not found"));

        Property property = propertyRepository.findById(request.getPropertyId())
                .orElseThrow(() -> new RuntimeException("Property not found"));

        ContactEnquiry enquiry = new ContactEnquiry();

        enquiry.setBuyer(buyer);
        enquiry.setProperty(property);
        enquiry.setMessage(request.getMessage());

        contactRepository.save(enquiry);

        return "Enquiry sent successfully";
    }

    public List<ContactEnquiry> getBuyerEnquiries(Long buyerId) {

        User buyer = userRepository.findById(buyerId)
                .orElseThrow(() -> new RuntimeException("Buyer not found"));

        return contactRepository.findByBuyer(buyer);
    }
}