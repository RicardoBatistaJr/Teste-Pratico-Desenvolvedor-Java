package com.teste.pratico.application.reservation.dto;

import com.teste.pratico.domain.entities.Reservation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class FinishReservationResponse {
    LocalDateTime startTime;
    LocalDateTime endTime;
    BigDecimal pricePerHour;
    Long hoursCharged;
    Long maintenanceFeePercentage;
    BigDecimal totalPrice;
}
