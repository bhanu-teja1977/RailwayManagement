package com.RailwayManagement.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "train_id", nullable = false)
    private Train train;

    @Column(nullable = false)
    private LocalDateTime bookingDate;

    @Column(nullable = false)
    private Integer seatsBooked;

    @Column(nullable = false)
    private BigDecimal totalFare;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status;

    @Version
    private Long version;

    public enum BookingStatus {
        CONFIRMED, CANCELLED
    }
}
