package com.teste.pratico.domain.exceptions;

public class ReservationNotFoundException extends RuntimeException {
    public ReservationNotFoundException(String message){
        super(message);
    }
}
