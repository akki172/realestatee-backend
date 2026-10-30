package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.ProfileResponse;
import com.example.demo.dto.UpdateProfileRequest;
import com.example.demo.service.ProfileService;

@RestController
@RequestMapping("/api/profile")
@CrossOrigin(origins = "http://localhost:5173")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfileResponse> getProfile(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                profileService.getProfile(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateProfile(
            @PathVariable Long id,
            @RequestBody UpdateProfileRequest request) {

        return ResponseEntity.ok(
                profileService.updateProfile(id, request)
        );
    }
}