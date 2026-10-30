package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Property;
import com.example.demo.entity.PropertyReview;
import com.example.demo.entity.User;

public interface PropertyReviewRepository
        extends JpaRepository<PropertyReview, Long> {

    List<PropertyReview> findByPropertyOrderByCreatedAtDesc(
            Property property
    );

    boolean existsByPropertyAndUser(
            Property property,
            User user
    );

    long countByProperty(Property property);
}