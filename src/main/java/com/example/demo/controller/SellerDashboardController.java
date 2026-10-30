package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.SellerDashboardResponse;
import com.example.demo.service.SellerDashboardService;

@RestController
@RequestMapping("/api/seller")
@CrossOrigin(origins = "http://localhost:5173")
public class SellerDashboardController {

    private final SellerDashboardService sellerDashboardService;

    public SellerDashboardController(
            SellerDashboardService sellerDashboardService) {

        this.sellerDashboardService = sellerDashboardService;
    }

    @GetMapping("/dashboard/{sellerId}")
    public ResponseEntity<SellerDashboardResponse> getDashboard(
            @PathVariable Long sellerId) {

        return ResponseEntity.ok(
                sellerDashboardService.getDashboard(sellerId)
        );
    }
}
