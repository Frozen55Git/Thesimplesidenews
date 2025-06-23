package com.thesimpleside.news.controller;

import com.thesimpleside.news.dto.UserLoginDto;
import com.thesimpleside.news.dto.UserProfileDto;
import com.thesimpleside.news.dto.UserRegistrationDto;
import com.thesimpleside.news.model.User;
import com.thesimpleside.news.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        if (model.containsAttribute("userRegistrationDto")) {
            return "auth/register";
        }
        model.addAttribute("userRegistrationDto", new UserRegistrationDto());
        return "auth/register";
    }
    
    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("userRegistrationDto") UserRegistrationDto registrationDto,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes) {
        
        log.info("Processing registration for email: {}", registrationDto.getEmail());
        
        // Check if passwords match
        if (!registrationDto.getPassword().equals(registrationDto.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "error.confirmPassword", "Passwords do not match");
        }
        
        // Check if email already exists
        if (userService.emailExists(registrationDto.getEmail())) {
            bindingResult.rejectValue("email", "error.email", "Email already exists");
        }
        
        if (bindingResult.hasErrors()) {
            log.warn("Registration validation failed for email: {}", registrationDto.getEmail());
            redirectAttributes.addFlashAttribute("userRegistrationDto", registrationDto);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userRegistrationDto", bindingResult);
            return "redirect:/register";
        }
        
        try {
            // Create user entity
            User user = User.builder()
                    .firstName(registrationDto.getFirstName())
                    .lastName(registrationDto.getLastName())
                    .email(registrationDto.getEmail())
                    .password(registrationDto.getPassword())
                    .build();
            
            User savedUser = userService.createUser(user);
            
            log.info("User registered successfully with ID: {}", savedUser.getId());
            
            redirectAttributes.addFlashAttribute("successMessage", 
                "Registration successful! Please log in with your credentials.");
            
            return "redirect:/login";
            
        } catch (Exception e) {
            log.error("Error during user registration: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Registration failed. Please try again.");
            redirectAttributes.addFlashAttribute("userRegistrationDto", registrationDto);
            return "redirect:/register";
        }
    }
    
    @GetMapping("/login")
    public String showLoginForm(@RequestParam(value = "error", required = false) String error,
                               @RequestParam(value = "logout", required = false) String logout,
                               Model model) {
        
        if (error != null) {
            model.addAttribute("errorMessage", "Invalid email or password.");
        }
        
        if (logout != null) {
            model.addAttribute("successMessage", "You have been logged out successfully.");
        }
        
        model.addAttribute("userLoginDto", new UserLoginDto());
        return "auth/login";
    }
    
    @PostMapping("/login")
    public String loginUser(@Valid @ModelAttribute("userLoginDto") UserLoginDto loginDto,
                           BindingResult bindingResult,
                           HttpServletRequest request,
                           RedirectAttributes redirectAttributes) {
        
        log.info("Processing login for email: {}", loginDto.getEmail());
        
        if (bindingResult.hasErrors()) {
            log.warn("Login validation failed for email: {}", loginDto.getEmail());
            return "auth/login";
        }
        
        try {
            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
            );
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            log.info("User logged in successfully: {}", loginDto.getEmail());
            
            return "redirect:/dashboard";
            
        } catch (Exception e) {
            log.warn("Login failed for email: {} - {}", loginDto.getEmail(), e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid email or password.");
            redirectAttributes.addFlashAttribute("userLoginDto", loginDto);
            return "redirect:/login";
        }
    }
    
    @GetMapping("/profile")
    public String showUserProfile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        UserProfileDto profileDto = UserProfileDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole())
                .enabled(user.isEnabled())
                .build();
        
        model.addAttribute("userProfile", profileDto);
        return "auth/profile";
    }
    
    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        model.addAttribute("user", user);
        return "dashboard";
    }
} 