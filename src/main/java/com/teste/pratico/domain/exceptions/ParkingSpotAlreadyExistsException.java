package com.teste.pratico.domain.exceptions;

public class ParkingSpotAlreadyExistsException extends RuntimeException {
    public ParkingSpotAlreadyExistsException(String message) {
        super(message);
    }
}
