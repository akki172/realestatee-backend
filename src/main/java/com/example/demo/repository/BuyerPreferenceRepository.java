package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.BuyerPreference;
import com.example.demo.entity.User;

public interface BuyerPreferenceRepository
        extends JpaRepository<BuyerPreference, Long> {

    Optional<BuyerPreference> findByBuyer(User buyer);
}