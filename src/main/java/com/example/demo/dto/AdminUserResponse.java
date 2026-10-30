package com.example.demo.dto;

import com.example.demo.entity.Role;

public class AdminUserResponse {

    private Long id;

    private String fullName;

    private String email;

    private String phone;

    private Role role;

    private String profileImage;

    private boolean verified;

    private boolean active;


    // =========================================
    // DEFAULT CONSTRUCTOR
    // =========================================

    public AdminUserResponse() {
    }


    // =========================================
    // FULL CONSTRUCTOR
    // =========================================

    public AdminUserResponse(
            Long id,
            String fullName,
            String email,
            String phone,
            Role role,
            String profileImage,
            boolean verified,
            boolean active) {

        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.profileImage = profileImage;
        this.verified = verified;
        this.active = active;
    }


    // =========================================
    // ID
    // =========================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    // =========================================
    // FULL NAME
    // =========================================

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }


    // =========================================
    // EMAIL
    // =========================================

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    // =========================================
    // PHONE
    // =========================================

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    // =========================================
    // ROLE
    // =========================================

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }


    // =========================================
    // PROFILE IMAGE
    // =========================================

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }


    // =========================================
    // VERIFIED
    // =========================================

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }


    // =========================================
    // ACTIVE
    // =========================================

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}