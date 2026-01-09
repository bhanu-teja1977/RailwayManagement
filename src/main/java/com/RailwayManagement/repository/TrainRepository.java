package com.RailwayManagement.repository;

import com.RailwayManagement.entity.Train;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TrainRepository extends JpaRepository<Train, Long> {
    List<Train> findBySourceAndDestinationAndDepartureTimeAfterAndSeatsAvailableGreaterThanEqual(
            String source, String destination, LocalDateTime departureTime, int seatsAvailable);
    
    boolean existsByTrainNumber(String trainNumber);
    
    @Query("SELECT t FROM Train t WHERE " +
           "(:source IS NULL OR :source = '' OR LOWER(t.source) LIKE LOWER(CONCAT('%', :source, '%'))) AND " +
           "(:destination IS NULL OR :destination = '' OR LOWER(t.destination) LIKE LOWER(CONCAT('%', :destination, '%'))) AND " +
           "(:date IS NULL OR FUNCTION('DATE', t.departureTime) = FUNCTION('DATE', :date)) AND " +
           "t.active = true")
    List<Train> searchTrains(
            @Param("source") String source,
            @Param("destination") String destination,
            @Param("date") LocalDateTime date);
    
    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END FROM Booking b WHERE b.train.id = :trainId AND b.status = 'CONFIRMED'")
    boolean existsActiveBookingsByTrainId(@Param("trainId") Long trainId);
}
