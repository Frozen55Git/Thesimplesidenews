package com.thesimpleside.news.config;

import com.thesimpleside.news.model.Role;
import com.thesimpleside.news.model.User;
import com.thesimpleside.news.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    private final UserService userService;

    @EventListener(ApplicationReadyEvent.class)
    public void initializeDefaultUsers() {
        log.info("Starting data initialization...");
        
        try {
            // Create default admin user if it doesn't exist
            if (!userService.emailExists("admin@thesimpleside.com")) {
                User adminUser = User.builder()
                        .firstName("Admin")
                        .lastName("User")
                        .email("admin@thesimpleside.com")
                        .password("admin123")
                        .role(Role.ADMIN)
                        .enabled(true)
                        .accountNonExpired(true)
                        .accountNonLocked(true)
                        .credentialsNonExpired(true)
                        .build();

                User savedAdmin = userService.createUser(adminUser);
                log.info("Default admin user created successfully: {} with ID: {}", 
                        savedAdmin.getEmail(), savedAdmin.getId());
            } else {
                log.info("Admin user already exists: admin@thesimpleside.com");
            }

            // Create default test user if it doesn't exist
            if (!userService.emailExists("user@thesimpleside.com")) {
                User testUser = User.builder()
                        .firstName("Test")
                        .lastName("User")
                        .email("user@thesimpleside.com")
                        .password("user123")
                        .role(Role.USER)
                        .enabled(true)
                        .accountNonExpired(true)
                        .accountNonLocked(true)
                        .credentialsNonExpired(true)
                        .build();

                User savedTestUser = userService.createUser(testUser);
                log.info("Default test user created successfully: {} with ID: {}", 
                        savedTestUser.getEmail(), savedTestUser.getId());
            } else {
                log.info("Test user already exists: user@thesimpleside.com");
            }

            // Create premium user if it doesn't exist
            if (!userService.emailExists("premium@thesimpleside.com")) {
                User premiumUser = User.builder()
                        .firstName("Premium")
                        .lastName("User")
                        .email("premium@thesimpleside.com")
                        .password("premium123")
                        .role(Role.PREMIUM_USER)
                        .enabled(true)
                        .accountNonExpired(true)
                        .accountNonLocked(true)
                        .credentialsNonExpired(true)
                        .build();

                User savedPremiumUser = userService.createUser(premiumUser);
                log.info("Default premium user created successfully: {} with ID: {}", 
                        savedPremiumUser.getEmail(), savedPremiumUser.getId());
            } else {
                log.info("Premium user already exists: premium@thesimpleside.com");
            }

            log.info("Data initialization completed successfully");

        } catch (Exception e) {
            log.error("Error during data initialization: {}", e.getMessage(), e);
        }
    }
} 