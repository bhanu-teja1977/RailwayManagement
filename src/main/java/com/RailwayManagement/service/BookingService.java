package com.RailwayManagement.service;

import com.RailwayManagement.entity.*;
import com.RailwayManagement.exception.BookingException;
import com.RailwayManagement.exception.ResourceNotFoundException;
import com.RailwayManagement.exception.TrainNotAvailableException;
import com.RailwayManagement.repository.BookingRepository;
import com.RailwayManagement.repository.TrainRepository;
import com.RailwayManagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final TrainRepository trainRepository;
    private final TrainService trainService;
    private final AuditService auditService;

    @Autowired
    public BookingService(BookingRepository bookingRepository,
                         UserRepository userRepository,
                         TrainRepository trainRepository,
                         TrainService trainService,
                         AuditService auditService) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.trainRepository = trainRepository;
        this.trainService = trainService;
        this.auditService = auditService;
    }

    @Transactional
    public Booking createBooking(Long userId, Long trainId, int seatsToBook) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        Train train = trainRepository.findById(trainId)
            .orElseThrow(() -> new ResourceNotFoundException("Train not found with id: " + trainId));

        // Check if train is available for booking
        if (!trainService.isTrainAvailable(trainId, seatsToBook)) {
            throw new TrainNotAvailableException("Not enough seats available on the selected train");
        }

        // Check if departure time is in the future
        if (train.getDepartureTime().isBefore(LocalDateTime.now())) {
            throw new BookingException("Cannot book a train that has already departed");
        }

        // Create booking
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setTrain(train);
        booking.setBookingDate(LocalDateTime.now());
        booking.setSeatsBooked(seatsToBook);
        booking.setTotalFare(train.getFare().multiply(BigDecimal.valueOf(seatsToBook)));
        booking.setStatus(Booking.BookingStatus.CONFIRMED);

        // Update available seats
        trainService.updateSeatsAvailable(trainId, seatsToBook);

        // Save booking
        Booking savedBooking = bookingRepository.save(booking);

        // Log the booking creation
        auditService.logAction("BOOKING_CREATE", 
            String.format("Booking created for %d seats on train %s", seatsToBook, train.getTrainNumber()));

        return savedBooking;
    }

    @Transactional
    public void cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + bookingId));

        if (booking.getStatus() == Booking.BookingStatus.CANCELLED) {
            throw new BookingException("Booking is already cancelled");
        }

        // Check if the train has already departed
        if (booking.getTrain().getDepartureTime().isBefore(LocalDateTime.now())) {
            throw new BookingException("Cannot cancel booking for a train that has already departed");
        }

        // Update booking status
        booking.setStatus(Booking.BookingStatus.CANCELLED);
        bookingRepository.save(booking);

        // Release the seats
        trainService.releaseSeats(booking.getTrain().getId(), booking.getSeatsBooked());

        // Log the cancellation
        auditService.logAction("BOOKING_CANCEL", 
            String.format("Booking %d cancelled, %d seats released", bookingId, booking.getSeatsBooked()));
    }

    @Transactional(readOnly = true)
    public Page<Booking> getUserBookings(Long userId, Pageable pageable) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }
        return bookingRepository.findByUserId(userId, pageable);
    }

    @Transactional(readOnly = true)
    public Booking getBookingDetails(Long bookingId) {
        return bookingRepository.findById(bookingId)
            .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + bookingId));
    }

    @Transactional(readOnly = true)
    public List<Booking> getBookingsByTrainAndDateRange(Long trainId, LocalDateTime startDate, LocalDateTime endDate) {
        return bookingRepository.findByTrainIdAndBookingDateBetween(trainId, startDate, endDate);
    }

    @Transactional(readOnly = true)
    public boolean hasActiveBooking(Long userId, Long trainId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
            
        return !bookingRepository.findActiveBookingsByUserAndTrain(user, trainId).isEmpty();
    }
}
