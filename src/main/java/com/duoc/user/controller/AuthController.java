package com.duoc.user.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.duoc.user.dto.ProfileResponse;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @PostMapping("/login")
    public ProfileResponse login(Authentication authentication) {
        return profile(authentication);
    }
    @GetMapping("/profile")
    public ProfileResponse profile(Authentication authentication) {
        String role=authentication.getAuthorities().iterator().next().getAuthority().replaceFirst("^ROLE_", "");
        return new ProfileResponse(authentication.getName(), role);
    }
}
