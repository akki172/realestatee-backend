package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.SellerProfileResponse;
import com.example.demo.service.SellerProfileService;

@RestController
@RequestMapping("/api/seller-profile")
@CrossOrigin(origins = "http://localhost:5173")
public class SellerProfileController {

    private final SellerProfileService sellerProfileService;

    public SellerProfileController(
            SellerProfileService sellerProfileService) {

        this.sellerProfileService = sellerProfileService;
    }

    @GetMapping("/{sellerId}")
    public ResponseEntity<SellerProfileResponse> getSellerProfile(
            @PathVariable Long sellerId) {

        return ResponseEntity.ok(
                sellerProfileService.getSellerProfile(sellerId)
        );
    }
}