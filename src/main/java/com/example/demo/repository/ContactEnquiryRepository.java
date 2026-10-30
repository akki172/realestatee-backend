package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.ContactEnquiry;
import com.example.demo.entity.User;

public interface ContactEnquiryRepository
        extends JpaRepository<ContactEnquiry, Long> {

    List<ContactEnquiry> findByBuyer(User buyer);
    long countByPropertySeller(User seller);

}