package com.RailwayManagement.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "trains")
public class Train {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String trainNumber;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String source;

    @Column(nullable = false)
    private String destination;

    @Column(nullable = false)
    private LocalDateTime departureTime;

    @Column(nullable = false)
    private LocalDateTime arrivalTime;

    @Column(nullable = false)
    private Integer seatsAvailable;

    @Column(nullable = false)
    private BigDecimal fare;

    @Column(columnDefinition = "boolean default true")
    private boolean active = true;

    @Version
    private Long version;
}
