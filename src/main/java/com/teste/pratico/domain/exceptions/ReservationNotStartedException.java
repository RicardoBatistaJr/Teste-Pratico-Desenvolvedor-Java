package com.teste.pratico.domain.exceptions;

public class ReservationNotStartedException extends RuntimeException{
    public ReservationNotStartedException(String message){
        super(message);
    }
}
