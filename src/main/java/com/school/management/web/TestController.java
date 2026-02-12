package com.school.management.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Test controller for verifying Spring Boot is working
 */
@RestController
public class TestController {
    
    @GetMapping("/api/test")
    public ResponseEntity<Map<String, Object>> test() {
        Map<String, Object> response = Map.of(
            "message", "Spring Boot is working!",
            "status", "SUCCESS",
            "timestamp", LocalDateTime.now().toString(),
            "endpoints", Map.of(
                "health", "/api/health",
                "h2-console", "/h2-console"
            )
        );
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/api/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of(
            "status", "UP",
            "service", "School Management System",
            "version", "1.0.0"
        ));
    }
}