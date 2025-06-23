package com.thesimpleside.news.service.impl;

import com.thesimpleside.news.model.User;
import com.thesimpleside.news.repository.UserRepository;
import com.thesimpleside.news.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("Loading user by email: {}", email);
        
        try {
            Optional<User> userOpt = userRepository.findByEmailAndEnabledTrue(email);
            
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                log.info("User found: {} (ID: {}, Role: {}, Enabled: {})", 
                        user.getEmail(), user.getId(), user.getRole(), user.isEnabled());
                return user;
            } else {
                log.warn("User not found or not enabled: {}", email);
                throw new UsernameNotFoundException("User not found with email: " + email);
            }
        } catch (Exception e) {
            log.error("Error loading user by email: {} - {}", email, e.getMessage(), e);
            throw new UsernameNotFoundException("Error loading user with email: " + email, e);
        }
    }
    
    @Override
    @Transactional
    public User createUser(User user) {
        log.info("Creating new user with email: {}", user.getEmail());
        
        try {
            if (emailExists(user.getEmail())) {
                throw new IllegalArgumentException("Email already exists: " + user.getEmail());
            }
            
            // Encode password before saving
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
            
            // Set default role if not specified
            if (user.getRole() == null) {
                user.setRole(com.thesimpleside.news.model.Role.USER);
            }
            
            // Ensure account is enabled by default
            user.setEnabled(true);
            user.setAccountNonExpired(true);
            user.setAccountNonLocked(true);
            user.setCredentialsNonExpired(true);
            
            User savedUser = userRepository.save(user);
            log.info("User created successfully with ID: {}", savedUser.getId());
            
            // Verify the user was actually saved by trying to retrieve it
            Optional<User> verifyUser = userRepository.findById(savedUser.getId());
            if (verifyUser.isEmpty()) {
                log.error("User was not properly saved to database. ID: {}", savedUser.getId());
                throw new RuntimeException("User creation failed - user not found in database after save");
            }
            
            return savedUser;
            
        } catch (Exception e) {
            log.error("Error during user creation: {}", e.getMessage(), e);
            throw e;
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        log.debug("Finding user by ID: {}", id);
        return userRepository.findById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        log.debug("Finding user by email: {}", email);
        return userRepository.findByEmail(email);
    }
    
    @Override
    @Transactional
    public User updateUser(User user) {
        log.info("Updating user with ID: {}", user.getId());
        
        User existingUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + user.getId()));
        
        // Update fields but preserve password and role unless explicitly changed
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setEmail(user.getEmail());
        
        // Only update password if a new one is provided
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        
        // Only update role if provided
        if (user.getRole() != null) {
            existingUser.setRole(user.getRole());
        }
        
        User updatedUser = userRepository.save(existingUser);
        log.info("User updated successfully with ID: {}", updatedUser.getId());
        
        return updatedUser;
    }
    
    @Override
    @Transactional
    public void deleteUser(Long id) {
        log.info("Deleting user with ID: {}", id);
        
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User not found with ID: " + id);
        }
        
        userRepository.deleteById(id);
        log.info("User deleted successfully with ID: {}", id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        log.debug("Retrieving all users");
        return userRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<User> getEnabledUsers() {
        log.debug("Retrieving all enabled users");
        return userRepository.findEnabledUsers();
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean emailExists(String email) {
        log.debug("Checking if email exists: {}", email);
        return userRepository.existsByEmail(email);
    }
    
    @Override
    @Transactional
    public User toggleUserStatus(Long userId, boolean enabled) {
        log.info("Toggling user status for ID: {} to enabled: {}", userId, enabled);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        
        user.setEnabled(enabled);
        User updatedUser = userRepository.save(user);
        
        log.info("User status updated successfully for ID: {}", userId);
        return updatedUser;
    }
    
    @Override
    @Transactional
    public void changePassword(Long userId, String newPassword) {
        log.info("Changing password for user ID: {}", userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        
        log.info("Password changed successfully for user ID: {}", userId);
    }
    
    @Override
    @Transactional
    public User updateUserRole(Long userId, com.thesimpleside.news.model.Role role) {
        log.info("Updating role for user ID: {} to role: {}", userId, role);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        
        user.setRole(role);
        User updatedUser = userRepository.save(user);
        
        log.info("User role updated successfully for ID: {}", userId);
        return updatedUser;
    }
    
    /**
     * Test method to verify password encoding and matching
     */
    public boolean testPasswordMatch(String email, String rawPassword) {
        log.info("Testing password match for email: {}", email);
        
        try {
            Optional<User> userOpt = userRepository.findByEmail(email);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                boolean matches = passwordEncoder.matches(rawPassword, user.getPassword());
                log.info("Password match result for {}: {}", email, matches);
                return matches;
            } else {
                log.warn("User not found for password test: {}", email);
                return false;
            }
        } catch (Exception e) {
            log.error("Error testing password match: {}", e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Get user details for debugging (including encoded password)
     */
    public Map<String, Object> getUserDetailsForDebug(String email) {
        log.info("Getting user details for debug: {}", email);
        
        try {
            Optional<User> userOpt = userRepository.findByEmail(email);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                Map<String, Object> details = new HashMap<>();
                details.put("id", user.getId());
                details.put("email", user.getEmail());
                details.put("firstName", user.getFirstName());
                details.put("lastName", user.getLastName());
                details.put("role", user.getRole());
                details.put("enabled", user.isEnabled());
                details.put("accountNonExpired", user.isAccountNonExpired());
                details.put("accountNonLocked", user.isAccountNonLocked());
                details.put("credentialsNonExpired", user.isCredentialsNonExpired());
                details.put("encodedPassword", user.getPassword());
                details.put("passwordLength", user.getPassword() != null ? user.getPassword().length() : 0);
                return details;
            } else {
                log.warn("User not found for debug: {}", email);
                return Map.of("error", "User not found");
            }
        } catch (Exception e) {
            log.error("Error getting user details for debug: {}", e.getMessage(), e);
            return Map.of("error", e.getMessage());
        }
    }
    
    /**
     * Encode a password for testing purposes
     */
    public String encodePassword(String rawPassword) {
        log.info("Encoding password for testing");
        return passwordEncoder.encode(rawPassword);
    }
    
    /**
     * Reset user password for testing purposes
     */
    @Transactional
    public boolean resetPassword(String email, String newPassword) {
        log.info("Resetting password for email: {}", email);
        
        try {
            Optional<User> userOpt = userRepository.findByEmail(email);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                String encodedPassword = passwordEncoder.encode(newPassword);
                user.setPassword(encodedPassword);
                userRepository.save(user);
                log.info("Password reset successfully for email: {}", email);
                return true;
            } else {
                log.warn("User not found for password reset: {}", email);
                return false;
            }
        } catch (Exception e) {
            log.error("Error resetting password: {}", e.getMessage(), e);
            return false;
        }
    }
} 