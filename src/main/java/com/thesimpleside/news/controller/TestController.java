package com.thesimpleside.news.controller;

import com.thesimpleside.news.model.User;
import com.thesimpleside.news.service.UserService;
import com.thesimpleside.news.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
@Slf4j
public class TestController {

    private final UserService userService;
    private final UserServiceImpl userServiceImpl;

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "OK");
        response.put("message", "Application is running");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/users")
    public ResponseEntity<Map<String, Object>> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("count", users.size());
            response.put("users", users.stream()
                    .map(user -> Map.of(
                            "id", user.getId(),
                            "email", user.getEmail(),
                            "firstName", user.getFirstName(),
                            "lastName", user.getLastName(),
                            "role", user.getRole(),
                            "enabled", user.isEnabled()
                    ))
                    .toList());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error retrieving users: {}", e.getMessage(), e);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping("/users/{email}")
    public ResponseEntity<Map<String, Object>> getUserByEmail(@PathVariable String email) {
        try {
            var userOpt = userService.findByEmail(email);
            Map<String, Object> response = new HashMap<>();
            
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                response.put("status", "success");
                response.put("user", Map.of(
                        "id", user.getId(),
                        "email", user.getEmail(),
                        "firstName", user.getFirstName(),
                        "lastName", user.getLastName(),
                        "role", user.getRole(),
                        "enabled", user.isEnabled(),
                        "accountNonExpired", user.isAccountNonExpired(),
                        "accountNonLocked", user.isAccountNonLocked(),
                        "credentialsNonExpired", user.isCredentialsNonExpired()
                ));
                return ResponseEntity.ok(response);
            } else {
                response.put("status", "not_found");
                response.put("message", "User not found with email: " + email);
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            log.error("Error retrieving user by email: {}", e.getMessage(), e);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping("/email-exists/{email}")
    public ResponseEntity<Map<String, Object>> checkEmailExists(@PathVariable String email) {
        try {
            boolean exists = userService.emailExists(email);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("email", email);
            response.put("exists", exists);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error checking email existence: {}", e.getMessage(), e);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping("/debug/user/{email}")
    public ResponseEntity<Map<String, Object>> getUserDebugInfo(@PathVariable String email) {
        try {
            Map<String, Object> debugInfo = userServiceImpl.getUserDetailsForDebug(email);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("debugInfo", debugInfo);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error getting debug info: {}", e.getMessage(), e);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PostMapping("/test-password")
    public ResponseEntity<Map<String, Object>> testPasswordMatch(
            @RequestParam String email,
            @RequestParam String password) {
        try {
            boolean matches = userServiceImpl.testPasswordMatch(email, password);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("email", email);
            response.put("passwordMatches", matches);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error testing password match: {}", e.getMessage(), e);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PostMapping("/encode-password")
    public ResponseEntity<Map<String, Object>> encodePassword(@RequestParam String password) {
        try {
            String encodedPassword = userServiceImpl.encodePassword(password);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("originalPassword", password);
            response.put("encodedPassword", encodedPassword);
            response.put("passwordLength", encodedPassword.length());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error encoding password: {}", e.getMessage(), e);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, Object>> resetPassword(
            @RequestParam String email,
            @RequestParam String newPassword) {
        try {
            boolean success = userServiceImpl.resetPassword(email, newPassword);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("email", email);
            response.put("passwordReset", success);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error resetting password: {}", e.getMessage(), e);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
} 