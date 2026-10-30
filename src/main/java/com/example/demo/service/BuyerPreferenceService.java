package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.dto.BuyerPreferenceRequest;
import com.example.demo.dto.BuyerPreferenceResponse;
import com.example.demo.entity.BuyerPreference;
import com.example.demo.entity.PropertyType;
import com.example.demo.entity.SearchPurpose;
import com.example.demo.entity.User;
import com.example.demo.repository.BuyerPreferenceRepository;
import com.example.demo.repository.UserRepository;

@Service
public class BuyerPreferenceService {

    private final BuyerPreferenceRepository preferenceRepository;
    private final UserRepository userRepository;

    public BuyerPreferenceService(
            BuyerPreferenceRepository preferenceRepository,
            UserRepository userRepository) {

        this.preferenceRepository = preferenceRepository;
        this.userRepository = userRepository;
    }

    public BuyerPreferenceResponse savePreference(
            BuyerPreferenceRequest request) {

        User buyer = userRepository.findById(request.getBuyerId())
                .orElseThrow(() ->
                        new RuntimeException("Buyer not found"));

        BuyerPreference preference =
                preferenceRepository.findByBuyer(buyer)
                        .orElse(new BuyerPreference());

        preference.setBuyer(buyer);

        if (request.getPurpose() != null &&
                !request.getPurpose().isBlank()) {

            preference.setPurpose(
                    SearchPurpose.valueOf(
                            request.getPurpose().toUpperCase()
                    )
            );
        }

        preference.setCity(request.getCity());

        if (request.getPropertyType() != null &&
                !request.getPropertyType().isBlank()) {

            preference.setPropertyType(
                    PropertyType.valueOf(
                            request.getPropertyType().toUpperCase()
                    )
            );
        }

        preference.setMinPrice(request.getMinPrice());
        preference.setMaxPrice(request.getMaxPrice());
        preference.setLatitude(request.getLatitude());
        preference.setLongitude(request.getLongitude());
        preference.setRadius(request.getRadius());

        preferenceRepository.save(preference);

        return convertToResponse(preference);
    }

    public BuyerPreferenceResponse getPreference(Long buyerId) {

        User buyer = userRepository.findById(buyerId)
                .orElseThrow(() ->
                        new RuntimeException("Buyer not found"));

        BuyerPreference preference =
                preferenceRepository.findByBuyer(buyer)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Buyer preferences not found"
                                ));

        return convertToResponse(preference);
    }

    private BuyerPreferenceResponse convertToResponse(
            BuyerPreference preference) {

        BuyerPreferenceResponse response =
                new BuyerPreferenceResponse();

        response.setId(preference.getId());

        if (preference.getPurpose() != null) {
            response.setPurpose(
                    preference.getPurpose().name()
            );
        }

        response.setCity(preference.getCity());

        if (preference.getPropertyType() != null) {
            response.setPropertyType(
                    preference.getPropertyType().name()
            );
        }

        response.setMinPrice(preference.getMinPrice());
        response.setMaxPrice(preference.getMaxPrice());
        response.setLatitude(preference.getLatitude());
        response.setLongitude(preference.getLongitude());
        response.setRadius(preference.getRadius());

        return response;
    }
}