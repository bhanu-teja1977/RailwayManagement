package com.RailwayManagement.service;

import com.RailwayManagement.entity.Train;
import com.RailwayManagement.exception.ResourceNotFoundException;
import com.RailwayManagement.exception.TrainNotAvailableException;
import com.RailwayManagement.repository.TrainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class TrainService {

    private final TrainRepository trainRepository;

    @Autowired
    public TrainService(TrainRepository trainRepository) {
        this.trainRepository = trainRepository;
    }

    public Train addTrain(Train train) {
        if (trainRepository.existsByTrainNumber(train.getTrainNumber())) {
            throw new IllegalStateException("Train with number " + train.getTrainNumber() + " already exists");
        }
        return trainRepository.save(train);
    }

    public Train updateTrain(Long id, Train trainDetails) {
        return trainRepository.findById(id).map(train -> {
            if (trainDetails.getTrainNumber() != null && !trainDetails.getTrainNumber().equals(train.getTrainNumber())) {
                if (trainRepository.existsByTrainNumber(trainDetails.getTrainNumber())) {
                    throw new IllegalStateException("Train number already in use");
                }
                train.setTrainNumber(trainDetails.getTrainNumber());
            }
            if (trainDetails.getName() != null) train.setName(trainDetails.getName());
            if (trainDetails.getSource() != null) train.setSource(trainDetails.getSource());
            if (trainDetails.getDestination() != null) train.setDestination(trainDetails.getDestination());
            if (trainDetails.getDepartureTime() != null) train.setDepartureTime(trainDetails.getDepartureTime());
            if (trainDetails.getArrivalTime() != null) train.setArrivalTime(trainDetails.getArrivalTime());
            if (trainDetails.getSeatsAvailable() != null) train.setSeatsAvailable(trainDetails.getSeatsAvailable());
            if (trainDetails.getFare() != null) train.setFare(trainDetails.getFare());
            return trainRepository.save(train);
        }).orElseThrow(() -> new ResourceNotFoundException("Train not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<Train> searchTrains(String source, String destination, LocalDateTime date) {
        return trainRepository.searchTrains(
            source != null ? source.trim() : null,
            destination != null ? destination.trim() : null,
            date
        );
    }

    @Transactional(readOnly = true)
    public Page<Train> getAllTrains(Pageable pageable) {
        return trainRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Train getTrainById(Long id) {
        return trainRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Train not found with id: " + id));
    }

    public void deleteTrain(Long id) {
        Train train = trainRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Train not found with id: " + id));
        
        // Check if there are any active bookings for this train
        if (trainRepository.existsActiveBookingsByTrainId(id)) {
            throw new IllegalStateException("Cannot delete train with active bookings");
        }
        
        trainRepository.delete(train);
    }

    @Transactional
    public void updateSeatsAvailable(Long trainId, int seatsToBook) {
        Train train = getTrainById(trainId);
        
        if (train.getSeatsAvailable() < seatsToBook) {
            throw new TrainNotAvailableException("Not enough seats available");
        }
        
        train.setSeatsAvailable(train.getSeatsAvailable() - seatsToBook);
        trainRepository.save(train);
    }

    @Transactional
    public void releaseSeats(Long trainId, int seatsToRelease) {
        Train train = getTrainById(trainId);
        train.setSeatsAvailable(train.getSeatsAvailable() + seatsToRelease);
        trainRepository.save(train);
    }

    @Transactional(readOnly = true)
    public boolean isTrainAvailable(Long trainId, int requiredSeats) {
        return trainRepository.findById(trainId)
            .map(train -> train.getSeatsAvailable() >= requiredSeats)
            .orElse(false);
    }
}
