package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.BuyerPreferenceRequest;
import com.example.demo.dto.BuyerPreferenceResponse;
import com.example.demo.service.BuyerPreferenceService;

@RestController
@RequestMapping("/api/buyer-preference")
@CrossOrigin(origins = "http://localhost:5173")
public class BuyerPreferenceController {

    private final BuyerPreferenceService preferenceService;

    public BuyerPreferenceController(
            BuyerPreferenceService preferenceService) {

        this.preferenceService = preferenceService;
    }

    @PostMapping("/save")
    public ResponseEntity<BuyerPreferenceResponse> savePreference(
            @RequestBody BuyerPreferenceRequest request) {

        return ResponseEntity.ok(
                preferenceService.savePreference(request)
        );
    }

    @GetMapping("/{buyerId}")
    public ResponseEntity<BuyerPreferenceResponse> getPreference(
            @PathVariable Long buyerId) {

        return ResponseEntity.ok(
                preferenceService.getPreference(buyerId)
        );
    }
}