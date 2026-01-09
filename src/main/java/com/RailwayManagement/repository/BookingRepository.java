package com.RailwayManagement.repository;

import com.RailwayManagement.entity.Booking;
import com.RailwayManagement.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserOrderByBookingDateDesc(User user);
    
    Page<Booking> findByUserId(Long userId, Pageable pageable);
    
    @Query("SELECT b FROM Booking b WHERE b.user = :user AND b.train.id = :trainId AND b.status = 'CONFIRMED'")
    List<Booking> findActiveBookingsByUserAndTrain(
            @Param("user") User user,
            @Param("trainId") Long trainId);
            
    @Query("SELECT COUNT(b) > 0 FROM Booking b WHERE b.train.id = :trainId AND b.status = 'CONFIRMED'")
    boolean existsActiveBookingsByTrainId(@Param("trainId") Long trainId);
    
    @Query("SELECT b FROM Booking b WHERE b.bookingDate BETWEEN :startDate AND :endDate")
    List<Booking> findBookingsBetweenDates(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
            
    List<Booking> findByTrainIdAndBookingDateBetween(Long trainId, LocalDateTime startDate, LocalDateTime endDate);
}
