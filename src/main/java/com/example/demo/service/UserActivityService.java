package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.entity.ActivityType;
import com.example.demo.entity.Property;
import com.example.demo.entity.User;
import com.example.demo.entity.UserActivity;
import com.example.demo.repository.PropertyRepository;
import com.example.demo.repository.UserActivityRepository;
import com.example.demo.repository.UserRepository;

@Service
public class UserActivityService {

    private final UserActivityRepository activityRepository;
    private final UserRepository userRepository;
    private final PropertyRepository propertyRepository;


    public UserActivityService(
            UserActivityRepository activityRepository,
            UserRepository userRepository,
            PropertyRepository propertyRepository) {

        this.activityRepository = activityRepository;
        this.userRepository = userRepository;
        this.propertyRepository = propertyRepository;
    }


    // =========================================
    // RECORD ACTIVITY
    // =========================================

    public void recordActivity(
            Long userId,
            Long propertyId,
            ActivityType activityType,
            String location) {


        UserActivity activity =
                new UserActivity();


        // =========================================
        // USER
        // =========================================

        if (userId != null) {

            User user =
                    userRepository.findById(userId)
                            .orElse(null);

            activity.setUser(user);
        }


        // =========================================
        // PROPERTY
        // =========================================

        if (propertyId != null) {

            Property property =
                    propertyRepository.findById(propertyId)
                            .orElse(null);

            activity.setProperty(property);
        }


        // =========================================
        // ACTIVITY DETAILS
        // =========================================

        activity.setActivityType(
                activityType
        );

        activity.setLocation(
                location
        );


        // =========================================
        // SAVE
        // =========================================

        activityRepository.save(activity);
    }
}