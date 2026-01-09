package com.RailwayManagement.controller;

import com.RailwayManagement.entity.Booking;
import com.RailwayManagement.entity.Train;
import com.RailwayManagement.entity.User;
import com.RailwayManagement.service.BookingService;
import com.RailwayManagement.service.TrainService;
import com.RailwayManagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/user/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private TrainService trainService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String listBookings(Authentication authentication,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size,
                              Model model) {
        String email = authentication.getName();
        User user = userService.getUserByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

        Page<Booking> bookingPage = bookingService.getUserBookings(user.getId(), PageRequest.of(page, size));
        model.addAttribute("bookings", bookingPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", bookingPage.getTotalPages());
        
        return "bookings";
    }

    @GetMapping("/book/{trainId}")
    public String showBookingForm(@PathVariable Long trainId, Model model, RedirectAttributes redirectAttributes) {
        try {
            Train train = trainService.getTrainById(trainId);
            model.addAttribute("train", train);
            return "booking-form";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Train not found");
            return "redirect:/trains";
        }
    }

    @PostMapping("/book/{trainId}")
    public String createBooking(@PathVariable Long trainId,
                               @RequestParam int seatsBooked,
                               Authentication authentication,
                               RedirectAttributes redirectAttributes) {
        try {
            String email = authentication.getName();
            User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

            Booking booking = bookingService.createBooking(user.getId(), trainId, seatsBooked);
            redirectAttributes.addFlashAttribute("message", "Booking created successfully!");
            return "redirect:/user/bookings/payment/" + booking.getId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Booking failed: " + e.getMessage());
            return "redirect:/user/bookings/book/" + trainId;
        }
    }

    @GetMapping("/{id}")
    public String viewBooking(@PathVariable Long id, Authentication authentication, Model model, RedirectAttributes redirectAttributes) {
        try {
            String email = authentication.getName();
            User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

            Booking booking = bookingService.getBookingDetails(id);
            
            // Check if the booking belongs to the current user
            if (!booking.getUser().getId().equals(user.getId())) {
                redirectAttributes.addFlashAttribute("error", "Access denied");
                return "redirect:/user/bookings";
            }

            model.addAttribute("booking", booking);
            return "booking-details";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Booking not found");
            return "redirect:/user/bookings";
        }
    }

    @PostMapping("/cancel/{id}")
    public String cancelBooking(@PathVariable Long id, Authentication authentication, RedirectAttributes redirectAttributes) {
        try {
            String email = authentication.getName();
            User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

            Booking booking = bookingService.getBookingDetails(id);
            
            // Check if the booking belongs to the current user
            if (!booking.getUser().getId().equals(user.getId())) {
                redirectAttributes.addFlashAttribute("error", "Access denied");
                return "redirect:/user/bookings";
            }

            bookingService.cancelBooking(id);
            redirectAttributes.addFlashAttribute("message", "Booking cancelled successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to cancel booking: " + e.getMessage());
        }
        return "redirect:/user/bookings";
    }
}
