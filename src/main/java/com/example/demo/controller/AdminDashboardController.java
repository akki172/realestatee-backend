package com.example.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.AdminActivityResponse;
import com.example.demo.dto.AdminDashboardResponse;
import com.example.demo.dto.AdminLocationActivityResponse;
import com.example.demo.dto.AdminTransactionResponse;
import com.example.demo.dto.AdminUserResponse;
import com.example.demo.dto.PropertyResponse;
import com.example.demo.service.AdminDashboardService;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:5173")
public class AdminDashboardController {

    private final AdminDashboardService adminDashboardService;


    public AdminDashboardController(
            AdminDashboardService adminDashboardService) {

        this.adminDashboardService =
                adminDashboardService;
    }


    // =========================================
    // ADMIN DASHBOARD
    // =========================================

    @GetMapping("/dashboard")
    public ResponseEntity<AdminDashboardResponse> getDashboard() {

        return ResponseEntity.ok(
                adminDashboardService.getDashboard()
        );
    }


    // =========================================
    // GET ALL USERS
    // =========================================

    @GetMapping("/users")
    public ResponseEntity<List<AdminUserResponse>> getAllUsers() {

        return ResponseEntity.ok(
                adminDashboardService.getAllUsers()
        );
    }


    // =========================================
    // GET USER BY ID
    // =========================================

    @GetMapping("/users/{id}")
    public ResponseEntity<AdminUserResponse> getUserById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                adminDashboardService.getUserById(id)
        );
    }


    // =========================================
    // ACTIVATE USER
    // =========================================

    @PutMapping("/users/{id}/activate")
    public ResponseEntity<String> activateUser(
            @PathVariable Long id) {

        adminDashboardService.activateUser(id);

        return ResponseEntity.ok(
                "User activated successfully"
        );
    }


    // =========================================
    // DEACTIVATE USER
    // =========================================

    @PutMapping("/users/{id}/deactivate")
    public ResponseEntity<String> deactivateUser(
            @PathVariable Long id) {

        adminDashboardService.deactivateUser(id);

        return ResponseEntity.ok(
                "User deactivated successfully"
        );
    }


    // =========================================
    // DELETE USER
    // =========================================

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(
            @PathVariable Long id) {

        adminDashboardService.deleteUser(id);

        return ResponseEntity.ok(
                "User deleted successfully"
        );
    }


    // =========================================
    // GET ALL PROPERTIES
    // =========================================

    @GetMapping("/properties")
    public ResponseEntity<List<PropertyResponse>> getAllProperties() {

        return ResponseEntity.ok(
                adminDashboardService.getAllProperties()
        );
    }


    // =========================================
    // DELETE PROPERTY
    // =========================================

    @DeleteMapping("/properties/{id}")
    public ResponseEntity<String> deleteProperty(
            @PathVariable Long id) {

        adminDashboardService.deleteProperty(id);

        return ResponseEntity.ok(
                "Property deleted successfully"
        );
    }
 // =========================================
 // VERIFY PROPERTY
 // =========================================

 @PutMapping("/properties/{id}/verify")
 public ResponseEntity<String> verifyProperty(
         @PathVariable Long id) {

     adminDashboardService.verifyProperty(id);

     return ResponseEntity.ok(
             "Property verified successfully"
     );
 }


 // =========================================
 // UNVERIFY PROPERTY
 // =========================================

 @PutMapping("/properties/{id}/unverify")
 public ResponseEntity<String> unverifyProperty(
         @PathVariable Long id) {

     adminDashboardService.unverifyProperty(id);

     return ResponseEntity.ok(
             "Property unverified successfully"
     );
 }
//=========================================
//GET ALL TRANSACTIONS
//=========================================

@GetMapping("/transactions")
public List<AdminTransactionResponse> getAllTransactions() {

  return adminDashboardService.getAllTransactions();
}
@GetMapping("/analytics/activity")
public ResponseEntity<List<AdminActivityResponse>>
        getActivityAnalytics() {

    return ResponseEntity.ok(
            adminDashboardService
                    .getActivityAnalytics()
    );
}
@GetMapping("/analytics/location")
public ResponseEntity<List<AdminLocationActivityResponse>>
        getLocationAnalytics() {

    return ResponseEntity.ok(
            adminDashboardService
                    .getLocationAnalytics()
    );
}
}