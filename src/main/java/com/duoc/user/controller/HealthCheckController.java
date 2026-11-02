package com.duoc.user.controller;

import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {
    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("service", "users", "status", "active");
    }
}
