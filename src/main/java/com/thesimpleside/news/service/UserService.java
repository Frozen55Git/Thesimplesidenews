package com.thesimpleside.news.service;

import com.thesimpleside.news.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {
    
    /**
     * Create a new user
     */
    User createUser(User user);
    
    /**
     * Find user by ID
     */
    Optional<User> findById(Long id);
    
    /**
     * Find user by email
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Update user information
     */
    User updateUser(User user);
    
    /**
     * Delete user by ID
     */
    void deleteUser(Long id);
    
    /**
     * Get all users
     */
    List<User> getAllUsers();
    
    /**
     * Get all enabled users
     */
    List<User> getEnabledUsers();
    
    /**
     * Check if email exists
     */
    boolean emailExists(String email);
    
    /**
     * Enable/disable user account
     */
    User toggleUserStatus(Long userId, boolean enabled);
    
    /**
     * Change user password
     */
    void changePassword(Long userId, String newPassword);
    
    /**
     * Update user role
     */
    User updateUserRole(Long userId, com.thesimpleside.news.model.Role role);
} 