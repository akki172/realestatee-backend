package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.dto.SellerProfileResponse;
import com.example.demo.entity.User;
import com.example.demo.repository.PropertyRepository;
import com.example.demo.repository.UserRepository;

@Service
public class SellerProfileService {

    private final UserRepository userRepository;
    private final PropertyRepository propertyRepository;

    public SellerProfileService(
            UserRepository userRepository,
            PropertyRepository propertyRepository) {

        this.userRepository = userRepository;
        this.propertyRepository = propertyRepository;
    }

    public SellerProfileResponse getSellerProfile(Long sellerId) {

        User seller = userRepository.findById(sellerId)
                .orElseThrow(() ->
                        new RuntimeException("Seller not found")
                );

        SellerProfileResponse response =
                new SellerProfileResponse();

        response.setId(seller.getId());
        response.setFullName(seller.getFullName());
        response.setProfileImage(seller.getProfileImage());
        response.setRole(
                seller.getRole() != null
                        ? seller.getRole().name()
                        : "SELLER"
        );
        response.setVerified(seller.isVerified());

        response.setPhone(seller.getPhone());
        response.setEmail(seller.getEmail());

        response.setPropertyCount(
                propertyRepository.countBySeller(seller)
        );

        return response;
    }
}