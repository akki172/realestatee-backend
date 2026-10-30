package com.example.demo.security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
            .csrf(csrf -> csrf.disable())

            .cors(cors ->
                cors.configurationSource(
                    corsConfigurationSource()
                )
            )

            .sessionManagement(session ->
                session.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            )

            .authorizeHttpRequests(auth -> auth

                // =========================
                // PUBLIC AUTH APIs
                // =========================

                .requestMatchers(
                    "/api/auth/login",
                    "/api/auth/register",
                    "/api/auth/forgot-password",
                    "/api/auth/reset-password"
                ).permitAll()


                // =========================
                // PROPERTY APIs
                // =========================

                .requestMatchers(
                    HttpMethod.POST,
                    "/api/property/add"
                ).hasRole("SELLER")

                // Seller: BOOKED → SOLD
                .requestMatchers(
                    HttpMethod.PUT,
                    "/api/property/*/status"
                ).hasRole("SELLER")

                .requestMatchers(
                    HttpMethod.PUT,
                    "/api/property/**"
                ).authenticated()

                .requestMatchers(
                    HttpMethod.DELETE,
                    "/api/property/**"
                ).hasRole("SELLER")


                // =========================
                // PUBLIC PROPERTY APIs
                // =========================

                .requestMatchers(
                    "/api/property/search"
                ).permitAll()

                .requestMatchers(
                    HttpMethod.GET,
                    "/api/property/seller/**",
                    "/api/seller-profile/**"
                ).permitAll()


                // =========================
                // ADMIN
                // =========================

                .requestMatchers(
                    "/api/admin/**"
                ).hasRole("ADMIN")


                // =========================
                // NOTIFICATIONS
                // =========================

                .requestMatchers(
                    "/api/notifications/**"
                ).authenticated()


                // =========================
                // PAYMENTS
                // =========================

                .requestMatchers(
                    HttpMethod.POST,
                    "/api/payments/create"
                ).hasRole("BUYER")

                // Seller bookings
                .requestMatchers(
                    HttpMethod.GET,
                    "/api/payments/seller/bookings"
                ).hasRole("SELLER")

                // Buyer payment APIs
                .requestMatchers(
                    HttpMethod.GET,
                    "/api/payments/**"
                ).hasRole("BUYER")


                // =========================
                // EVERYTHING ELSE
                // =========================

                .anyRequest().authenticated()
            )

            .addFilterBefore(
                jwtFilter,
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }


    // =========================
    // CORS
    // =========================

    @Bean
    CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration =
            new CorsConfiguration();

        configuration.setAllowedOrigins(
        	    List.of(
        	        "http://localhost:5173",
        	        "https://realestatee-two.vercel.app"
        	    )
        	);

        configuration.setAllowedMethods(
            List.of(
                "GET",
                "POST",
                "PUT",
                "DELETE",
                "OPTIONS"
            )
        );

        configuration.setAllowedHeaders(
            List.of("*")
        );

        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
            new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration(
            "/**",
            configuration
        );

        return source;
    }


    // =========================
    // AUTHENTICATION MANAGER
    // =========================

    @Bean
    AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration)
            throws Exception {

        return configuration.getAuthenticationManager();
    }
}