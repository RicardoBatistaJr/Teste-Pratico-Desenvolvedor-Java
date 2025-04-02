package com.teste.pratico.application.reservation.dto;

import com.teste.pratico.domain.entities.Reservation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ReservationResponse {
    private Long id;
    private LocalDateTime startTime;
    private String parkingSpotCode;
    private String client;

    public static ReservationResponse from(Reservation reservation){
        return new ReservationResponse(reservation.getId(), reservation.getStartTime(), reservation.getParkingSpot().getCode(), reservation.getClient().getName());
    }
}
