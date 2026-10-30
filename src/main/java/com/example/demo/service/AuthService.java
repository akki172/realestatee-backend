package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.ForgetPasswordRequest;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.LoginResponse;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.dto.ResetPasswordRequest;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtService;

@Service
public class AuthService {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
   private final EmailService emailService;
    public AuthService(UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            EmailService emailService) {

this.userRepository = userRepository;
this.passwordEncoder = passwordEncoder;
this.jwtService = jwtService;
this.emailService = emailService;
}

    public String register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            return "Email already exists";
        }

        if (userRepository.existsByPhone(request.getPhone())) {
            return "Phone already exists";
        }

        User user = new User();

        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());

        // Encrypt password before saving
        user.setPassword(
                passwordEncoder.encode(request.getPassword()));

        user.setRole(
                Role.valueOf(request.getRole().toUpperCase()));

        userRepository.save(user);

        return "User Registered Successfully";
    }
    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElse(null);

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        boolean matches = passwordEncoder.matches(
                request.getPassword(),
                user.getPassword());

        if (!matches) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtService.generateToken(user.getEmail());

        return new LoginResponse(
                token,
                user.getId(),
                user.getEmail(),
                user.getRole().name()
        );
    }
    public String resetPassword(ResetPasswordRequest request) {

        User user = userRepository.findByResetToken(request.getToken())
                .orElseThrow(() ->
                        new RuntimeException("Invalid reset token"));

        // Check if expiry exists
        if (user.getTokenExpiry() == null) {
            throw new RuntimeException("Reset token expired");
        }

        // Check if token has expired
        if (user.getTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Reset token expired");
        }

        // Update password
        user.setPassword(
                passwordEncoder.encode(request.getNewPassword())
        );

        // Clear reset token after successful reset
        user.setResetToken(null);
        user.setTokenExpiry(null);

        userRepository.save(user);

        return "Password reset successful.";
    }
    public String forgetPassword(ForgetPasswordRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Email not found"));

        String token = UUID.randomUUID().toString();

        user.setResetToken(token);
        user.setTokenExpiry(LocalDateTime.now().plusMinutes(15));

        userRepository.save(user);

        String link = "http://localhost:5173/reset-password?token=" + token;

       
		emailService.sendEmail(
                user.getEmail(),
                "Reset Password - Real Estate Platform",
                "Click the link below to reset your password:\n\n" + link
        );

        return "Password reset link sent to your email.";
    }
}