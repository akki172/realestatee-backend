package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entity.ActivityType;
import com.example.demo.entity.UserActivity;

public interface UserActivityRepository
        extends JpaRepository<UserActivity, Long> {

    List<UserActivity> findByActivityType(
            ActivityType activityType
    );


    List<UserActivity> findByLocation(
            String location
    );


    long countByActivityType(
            ActivityType activityType
    );


    long countByLocation(
            String location
    );


    // =========================================
    // LOCATION ANALYTICS
    // =========================================

    @Query("""
        SELECT ua.location, COUNT(ua)
        FROM UserActivity ua
        WHERE ua.location IS NOT NULL
        GROUP BY ua.location
        ORDER BY COUNT(ua) DESC
    """)
    List<Object[]> getActivityByLocation();

}