package com.thesimpleside.news.repository;

import com.thesimpleside.news.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Find user by email address
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Check if user exists by email
     */
    boolean existsByEmail(String email);
    
    /**
     * Find user by email and enabled status
     */
    Optional<User> findByEmailAndEnabledTrue(String email);
    
    /**
     * Find users by role
     */
    @Query("SELECT u FROM User u WHERE u.role = :role")
    java.util.List<User> findByRole(@Param("role") com.thesimpleside.news.model.Role role);
    
    /**
     * Find enabled users
     */
    @Query("SELECT u FROM User u WHERE u.enabled = true")
    java.util.List<User> findEnabledUsers();
} 