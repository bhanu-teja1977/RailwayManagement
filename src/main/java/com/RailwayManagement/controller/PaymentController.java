package com.RailwayManagement.controller;

import com.RailwayManagement.entity.Booking;
import com.RailwayManagement.entity.Payment;
import com.RailwayManagement.entity.User;
import com.RailwayManagement.service.BookingService;
import com.RailwayManagement.service.PaymentService;
import com.RailwayManagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/user/bookings/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserService userService;

    @GetMapping("/{bookingId}")
    public String showPaymentForm(@PathVariable Long bookingId, 
                                 Authentication authentication,
                                 Model model, 
                                 RedirectAttributes redirectAttributes) {
        try {
            String email = authentication.getName();
            User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

            Booking booking = bookingService.getBookingDetails(bookingId);
            
            // Check if the booking belongs to the current user
            if (!booking.getUser().getId().equals(user.getId())) {
                redirectAttributes.addFlashAttribute("error", "Access denied");
                return "redirect:/user/bookings";
            }

            model.addAttribute("booking", booking);
            model.addAttribute("paymentModes", Payment.PaymentMode.values());
            return "payment";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Booking not found");
            return "redirect:/user/bookings";
        }
    }

    @PostMapping("/{bookingId}")
    public String processPayment(@PathVariable Long bookingId,
                                @RequestParam Payment.PaymentMode paymentMode,
                                @RequestParam(required = false) String paymentDetails,
                                Authentication authentication,
                                RedirectAttributes redirectAttributes) {
        try {
            String email = authentication.getName();
            User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

            Booking booking = bookingService.getBookingDetails(bookingId);
            
            // Check if the booking belongs to the current user
            if (!booking.getUser().getId().equals(user.getId())) {
                redirectAttributes.addFlashAttribute("error", "Access denied");
                return "redirect:/user/bookings";
            }

            Payment payment = paymentService.processPayment(bookingId, paymentMode, paymentDetails);
            redirectAttributes.addFlashAttribute("message", "Payment successful! Transaction ID: " + payment.getTransactionId());
            return "redirect:/user/bookings/" + bookingId;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Payment failed: " + e.getMessage());
            return "redirect:/user/bookings/payment/" + bookingId;
        }
    }
}
