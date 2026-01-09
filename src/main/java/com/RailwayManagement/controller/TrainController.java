package com.RailwayManagement.controller;

import com.RailwayManagement.entity.Train;
import com.RailwayManagement.service.AuditService;
import com.RailwayManagement.service.TrainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;
@Controller
public class TrainController {
    @Autowired
    private TrainService trainService;
    @Autowired
    private AuditService auditService;
    @GetMapping("/trains")
    public String listTrains(@RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "10") int size,
                            Model model) {
        Page<Train> trainPage = trainService.getAllTrains(PageRequest.of(page, size));
        model.addAttribute("trains", trainPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", trainPage.getTotalPages());
        return "trains";
    }
    @GetMapping("/trains/search")
    public String searchTrains(@RequestParam(required = false) String source,
                              @RequestParam(required = false) String destination,
                              @RequestParam(required = false) String date,
                              Model model) {
        LocalDateTime searchDate = null;
        try {
            if (date != null && !date.isEmpty()) {
                searchDate = LocalDateTime.parse(date + "T00:00:00");
            }
            List<Train> trains = trainService.searchTrains(source, destination, searchDate);
            model.addAttribute("trains", trains);
            model.addAttribute("source", source);
            model.addAttribute("destination", destination);
            model.addAttribute("date", date);
            return "trains";
        } catch (Exception e) {
            model.addAttribute("error", "Invalid date format. Please select a valid date.");
            model.addAttribute("trains", trainService.getAllTrains(PageRequest.of(0, 100)).getContent());
            return "trains";
        }
    }
    @GetMapping("/admin/trains")
    public String adminListTrains(@RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "10") int size,
                                  Model model) {
        Page<Train> trainPage = trainService.getAllTrains(PageRequest.of(page, size));
        model.addAttribute("trains", trainPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", trainPage.getTotalPages());
        return "admin/trains";
    }
    @GetMapping("/admin/trains/add")
    public String showAddTrainForm(Model model) {
        model.addAttribute("train", new Train());
        return "admin/train-form";
    }

    @PostMapping("/admin/trains/add")
    public String addTrain(@ModelAttribute Train train, RedirectAttributes redirectAttributes) {
        try {
            trainService.addTrain(train);
            auditService.logAction("TRAIN_CREATE", "Train created: " + train.getTrainNumber());
            redirectAttributes.addFlashAttribute("message", "Train added successfully!");
            return "redirect:/admin/trains";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to add train: " + e.getMessage());
            return "redirect:/admin/trains/add";
        }
    }

    @GetMapping("/admin/trains/edit/{id}")
    public String showEditTrainForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Train train = trainService.getTrainById(id);
            model.addAttribute("train", train);
            return "admin/train-form";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Train not found");
            return "redirect:/admin/trains";
        }
    }

    @PostMapping("/admin/trains/edit/{id}")
    public String updateTrain(@PathVariable Long id, @ModelAttribute Train train, RedirectAttributes redirectAttributes) {
        try {
            trainService.updateTrain(id, train);
            auditService.logAction("TRAIN_UPDATE", "Train updated: " + train.getTrainNumber());
            redirectAttributes.addFlashAttribute("message", "Train updated successfully!");
            return "redirect:/admin/trains";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update train: " + e.getMessage());
            return "redirect:/admin/trains/edit/" + id;
        }
    }

    @GetMapping("/admin/trains/delete/{id}")
    public String deleteTrain(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Train train = trainService.getTrainById(id);
            trainService.deleteTrain(id);
            auditService.logAction("TRAIN_DELETE", "Train deleted: " + train.getTrainNumber());
            redirectAttributes.addFlashAttribute("message", "Train deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete train: " + e.getMessage());
        }
        return "redirect:/admin/trains";
    }
}
