package com.example.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.demo.dto.NotificationResponse;
import com.example.demo.entity.Notification;
import com.example.demo.entity.User;
import com.example.demo.repository.NotificationRepository;
import com.example.demo.repository.UserRepository;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public NotificationService(
            NotificationRepository notificationRepository,
            UserRepository userRepository) {

        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }


    // =========================================
    // CREATE NOTIFICATION
    // =========================================

    public Notification createNotification(
            Long userId,
            String message,
            String type,
            Long propertyId,
            Long commentId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        Notification notification =
                new Notification();

        notification.setUser(user);
        notification.setMessage(message);
        notification.setType(type);
        notification.setPropertyId(propertyId);
        notification.setCommentId(commentId);
        notification.setRead(false);

        return notificationRepository.save(
                notification
        );
    }


    // =========================================
    // GET USER NOTIFICATIONS
    // =========================================

    public List<NotificationResponse> getNotifications(
            Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        return notificationRepository
                .findByUserOrderByCreatedAtDesc(user)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }


    // =========================================
    // GET UNREAD COUNT
    // =========================================

    public long getUnreadCount(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        return notificationRepository
                .countByUserAndReadFalse(user);
    }


    // =========================================
    // MARK AS READ
    // =========================================

    public void markAsRead(Long notificationId) {

        Notification notification =
                notificationRepository.findById(
                        notificationId
                ).orElseThrow(() ->
                        new RuntimeException(
                                "Notification not found"
                        )
                );

        notification.setRead(true);

        notificationRepository.save(notification);
    }


    // =========================================
    // MARK ALL AS READ
    // =========================================

    public void markAllAsRead(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        List<Notification> notifications =
                notificationRepository
                        .findByUserOrderByCreatedAtDesc(user);

        for (Notification notification :
                notifications) {

            notification.setRead(true);
        }

        notificationRepository.saveAll(
                notifications
        );
    }


    // =========================================
    // ENTITY → RESPONSE
    // =========================================

    private NotificationResponse convertToResponse(
            Notification notification) {

        NotificationResponse response =
                new NotificationResponse();

        response.setId(notification.getId());

        response.setMessage(
                notification.getMessage()
        );

        response.setType(
                notification.getType()
        );

        response.setPropertyId(
                notification.getPropertyId()
        );

        response.setCommentId(
                notification.getCommentId()
        );

        response.setRead(
                notification.isRead()
        );

        response.setCreatedAt(
                notification.getCreatedAt()
        );

        return response;
    }
}