package com.example.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.VisitBookingRequest;
import com.example.demo.dto.VisitDecisionRequest;
import com.example.demo.entity.VisitBooking;
import com.example.demo.service.VisitBookingService;

@RestController
@RequestMapping("/api/visit")
@CrossOrigin(origins = "http://localhost:5173")
public class VisitBookingController {

    private final VisitBookingService visitBookingService;

    public VisitBookingController(VisitBookingService visitBookingService) {
        this.visitBookingService = visitBookingService;
    }

    @PostMapping("/book")
    public ResponseEntity<String> bookVisit(
            @RequestBody VisitBookingRequest request) {

        return ResponseEntity.ok(
                visitBookingService.bookVisit(request)
        );
    }

    @GetMapping("/{buyerId}")
    public ResponseEntity<List<VisitBooking>> getVisits(
            @PathVariable Long buyerId) {

        return ResponseEntity.ok(
                visitBookingService.getVisits(buyerId)
        );
    }
    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<VisitBooking>> getSellerVisits(
            @PathVariable Long sellerId) {

        return ResponseEntity.ok(
                visitBookingService.getSellerVisits(sellerId)
        );

    }
    @PutMapping("/approve/{id}")
    public ResponseEntity<String> approveVisit(
            @PathVariable Long id,
            @RequestBody VisitDecisionRequest request) {

        return ResponseEntity.ok(
                visitBookingService.approveVisit(id, request.getComment())
        );
    }

    @PutMapping("/reject/{id}")
    public ResponseEntity<String> rejectVisit(
            @PathVariable Long id,
            @RequestBody VisitDecisionRequest request) {

        return ResponseEntity.ok(
                visitBookingService.rejectVisit(id, request.getComment())
        );
    }
    @PutMapping("/{id}/visited")
    public ResponseEntity<String> markAsVisited(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                visitBookingService.markAsVisited(id)
        );
    }
    @PutMapping("/{id}/interest")
    public ResponseEntity<String> updateBuyerInterest(
            @PathVariable Long id,
            @RequestParam boolean interested) {

        return ResponseEntity.ok(
                visitBookingService.updateBuyerInterest(
                        id,
                        interested
                )
        );
    }
    }