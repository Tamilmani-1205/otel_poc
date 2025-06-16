package com.productmanagement.controller;

import com.productmanagement.dto.auth.AuthResponse;
import com.productmanagement.dto.auth.LoginRequest;
import com.productmanagement.dto.auth.RegisterRequest;
import com.productmanagement.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        log.info("Received registration request for user: {}", request.getUsername());
        try {
            AuthResponse response = authService.register(request);
            log.info("Successfully registered user: {}", request.getUsername());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Failed to register user: {} - Error: {}", request.getUsername(), e.getMessage());
            throw e;
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        log.info("Received login request for user: {}", request.getUsername());
        try {
            AuthResponse response = authService.login(request);
            log.info("Successfully logged in user: {}", request.getUsername());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Failed to login user: {} - Error: {}", request.getUsername(), e.getMessage());
            throw e;
        }
    }
} 