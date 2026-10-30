package com.example.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.ContactRequest;
import com.example.demo.entity.ContactEnquiry;
import com.example.demo.service.ContactService;

@RestController
@RequestMapping("/api/contact")
@CrossOrigin(origins = "http://localhost:5173")
public class ContactController {

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendEnquiry(
            @RequestBody ContactRequest request) {

        return ResponseEntity.ok(
                contactService.sendEnquiry(request)
        );
    }

    @GetMapping("/{buyerId}")
    public ResponseEntity<List<ContactEnquiry>> getBuyerEnquiries(
            @PathVariable Long buyerId) {

        return ResponseEntity.ok(
                contactService.getBuyerEnquiries(buyerId)
        );
    }
}