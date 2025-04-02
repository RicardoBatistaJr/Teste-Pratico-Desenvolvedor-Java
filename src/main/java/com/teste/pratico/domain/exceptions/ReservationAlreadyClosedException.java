package com.teste.pratico.domain.exceptions;

public class ReservationAlreadyClosedException extends RuntimeException{
    public ReservationAlreadyClosedException(String message){
        super(message);
    }
}
