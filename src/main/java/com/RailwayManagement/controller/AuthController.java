package com.RailwayManagement.controller;

import com.RailwayManagement.entity.User;
import com.RailwayManagement.service.AuditService;
import com.RailwayManagement.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private AuditService auditService;
    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }
    @GetMapping("/login")
    public String showLoginForm(@RequestParam(value = "error", required = false) String error,
                               @RequestParam(value = "logout", required = false) String logout,
                               Model model) {
        if (error != null) {
            model.addAttribute("error", "Invalid email or password");
        }
        if (logout != null) {
            model.addAttribute("message", "You have been logged out successfully");
        }
        return "login";
    }
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }
    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") User user,
                              BindingResult result,
                              RedirectAttributes redirectAttributes,
                              Model model) {
        if (result.hasErrors()) {
            return "register";
        }
        try {
            if (userService.existsByEmail(user.getEmail())) {
                model.addAttribute("error", "Email already registered");
                return "register";
            }
            user.setRole(User.Role.USER);
            userService.registerUser(user);
            auditService.logAction("USER_REGISTRATION", "New user registered: " + user.getEmail());
            redirectAttributes.addFlashAttribute("message", "Registration successful! Please login.");
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("error", "Registration failed: " + e.getMessage());
            return "register";
        }
    }
    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied";
    }
}
