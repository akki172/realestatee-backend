package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.User;
import com.example.demo.entity.VisitBooking;
import com.example.demo.entity.VisitStatus;

public interface VisitBookingRepository extends JpaRepository<VisitBooking, Long> {

    List<VisitBooking> findByBuyer(User buyer);

    long countByPropertySellerAndStatus(User seller, VisitStatus status);
    List<VisitBooking> findByPropertySeller(User seller);

}