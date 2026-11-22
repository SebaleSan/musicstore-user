package com.duoc.user.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.duoc.user.dto.ProfileResponse;
import com.duoc.user.dto.ProfileUpdateRequest;
import com.duoc.user.dto.RegisterRequest;
import com.duoc.user.dto.UserResponse;
import com.duoc.user.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService service;
    public AuthController(UserService service) {
        this.service=service;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.register(request));
    }

    @PostMapping("/login")
    public UserResponse login(Authentication authentication) {
        return service.findByUsername(authentication.getName());
    }
    @GetMapping("/profile")
    public ProfileResponse profile(Authentication authentication) {
        String role=authentication.getAuthorities().iterator().next().getAuthority().replaceFirst("^ROLE_", "");
        return new ProfileResponse(authentication.getName(), role);
    }
    @PutMapping("/profile")
    public UserResponse updateProfile(Authentication authentication, @Valid @RequestBody ProfileUpdateRequest request) {
        return service.updateProfile(authentication.getName(), request);
    }
}
