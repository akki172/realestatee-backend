package com.example.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.NotificationResponse;
import com.example.demo.service.NotificationService;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "http://localhost:5173")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(
            NotificationService notificationService) {

        this.notificationService = notificationService;
    }


    // =========================================
    // GET USER NOTIFICATIONS
    // =========================================

    @GetMapping("/{userId}")
    public ResponseEntity<List<NotificationResponse>>
    getNotifications(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                notificationService.getNotifications(userId)
        );
    }


    // =========================================
    // GET UNREAD COUNT
    // =========================================

    @GetMapping("/{userId}/unread-count")
    public ResponseEntity<Long> getUnreadCount(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                notificationService.getUnreadCount(userId)
        );
    }


    // =========================================
    // MARK ONE AS READ
    // =========================================

    @PutMapping("/{notificationId}/read")
    public ResponseEntity<String> markAsRead(
            @PathVariable Long notificationId) {

        notificationService.markAsRead(
                notificationId
        );

        return ResponseEntity.ok(
                "Notification marked as read"
        );
    }


    // =========================================
    // MARK ALL AS READ
    // =========================================

    @PutMapping("/{userId}/read-all")
    public ResponseEntity<String> markAllAsRead(
            @PathVariable Long userId) {

        notificationService.markAllAsRead(
                userId
        );

        return ResponseEntity.ok(
                "All notifications marked as read"
        );
    }
}