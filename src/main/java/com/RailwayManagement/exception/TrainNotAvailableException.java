package com.RailwayManagement.exception;

public class TrainNotAvailableException extends RuntimeException {
    public TrainNotAvailableException(String message) {
        super(message);
    }
}
