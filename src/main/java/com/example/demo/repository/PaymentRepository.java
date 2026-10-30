package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Payment;
import com.example.demo.entity.PaymentStatus;

public interface PaymentRepository
        extends JpaRepository<Payment, Long> {

    Optional<Payment> findByOrderId(String orderId);

    Optional<Payment> findByPaymentId(String paymentId);

    List<Payment> findByBuyerId(Long buyerId);

    List<Payment> findByPropertyId(Long propertyId);

    List<Payment> findByStatus(PaymentStatus status);
    List<Payment> findByPropertySellerId(Long sellerId);

}