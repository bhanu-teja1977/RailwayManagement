package com.RailwayManagement.service;

import com.RailwayManagement.entity.*;
import com.RailwayManagement.exception.PaymentException;
import com.RailwayManagement.exception.ResourceNotFoundException;
import com.RailwayManagement.repository.BookingRepository;
import com.RailwayManagement.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final AuditService auditService;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository, 
                         BookingRepository bookingRepository,
                         AuditService auditService) {
        this.paymentRepository = paymentRepository;
        this.bookingRepository = bookingRepository;
        this.auditService = auditService;
    }

    @Transactional
    public Payment processPayment(Long bookingId, Payment.PaymentMode paymentMode, String paymentDetails) {
        // In a real application, you would integrate with a payment gateway here
        // For this example, we'll simulate a successful payment
        
        Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + bookingId));

        // Check if booking is already paid
        if (paymentRepository.existsByBooking(booking)) {
            throw new PaymentException("Payment already processed for this booking");
        }

        // Check if booking is cancelled
        if (booking.getStatus() == Booking.BookingStatus.CANCELLED) {
            throw new PaymentException("Cannot process payment for a cancelled booking");
        }

        // Create payment record
        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setPaymentMode(paymentMode);
        payment.setAmount(booking.getTotalFare());
        payment.setPaymentDate(LocalDateTime.now());
        
        // Generate a unique transaction ID (in real app, this would come from payment gateway)
        String transactionId = "TXN" + UUID.randomUUID().toString().replace("-", "").substring(0, 15).toUpperCase();
        payment.setTransactionId(transactionId);

        // Save payment
        Payment savedPayment = paymentRepository.save(payment);

        // Log the payment
        auditService.logAction("PAYMENT_PROCESSED", 
            String.format("Payment of %.2f processed for booking %d", 
                payment.getAmount(), booking.getId()));

        return savedPayment;
    }

    @Transactional(readOnly = true)
    public Payment getPaymentByBookingId(Long bookingId) {
        return paymentRepository.findByBookingId(bookingId)
            .orElseThrow(() -> new ResourceNotFoundException("Payment not found for booking id: " + bookingId));
    }

    @Transactional(readOnly = true)
    public Payment getPaymentByTransactionId(String transactionId) {
        return paymentRepository.findByTransactionId(transactionId)
            .orElseThrow(() -> new ResourceNotFoundException("Payment not found with transaction id: " + transactionId));
    }

    @Transactional
    public void issueRefund(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
            .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + paymentId));

        // In a real application, you would integrate with a payment gateway to process the refund
        // For this example, we'll just update the payment status
        
        // Log the refund
        auditService.logAction("REFUND_ISSUED", 
            String.format("Refund of %.2f processed for payment %s", 
                payment.getAmount(), payment.getTransactionId()));
    }
}
