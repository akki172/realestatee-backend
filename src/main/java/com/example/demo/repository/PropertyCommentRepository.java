package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.PropertyComment;

public interface PropertyCommentRepository
        extends JpaRepository<PropertyComment, Long> {

    List<PropertyComment> findByPropertyIdOrderByCreatedAtDesc(
            Long propertyId
    );

    List<PropertyComment>
    findByPropertySellerIdOrderByCreatedAtDesc(
            Long sellerId
    );

    List<PropertyComment>
    findByUserIdOrderByCreatedAtDesc(
            Long userId
    );

    long countByPropertySellerId(Long sellerId);
}