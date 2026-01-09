package com.RailwayManagement.controller;

import com.RailwayManagement.entity.User;
import com.RailwayManagement.service.BookingService;
import com.RailwayManagement.service.TrainService;
import com.RailwayManagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @Autowired
    private UserService userService;

    @Autowired
    private TrainService trainService;

    @Autowired
    private BookingService bookingService;

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        String email = authentication.getName();
        User user = userService.getUserByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

        model.addAttribute("user", user);

        if (user.getRole() == User.Role.ADMIN) {
            return "redirect:/admin/dashboard";
        } else {
            return "redirect:/user/dashboard";
        }
    }

    @GetMapping("/user/dashboard")
    public String userDashboard(Authentication authentication, Model model) {
        String email = authentication.getName();
        User user = userService.getUserByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

        model.addAttribute("user", user);
        model.addAttribute("recentBookings", bookingService.getUserBookings(user.getId(), PageRequest.of(0, 5)));
        
        return "dashboard";
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard(Authentication authentication, Model model) {
        String email = authentication.getName();
        User user = userService.getUserByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

        model.addAttribute("user", user);
        model.addAttribute("totalTrains", trainService.getAllTrains(PageRequest.of(0, Integer.MAX_VALUE)).getTotalElements());
        
        return "admin-dashboard";
    }
}
