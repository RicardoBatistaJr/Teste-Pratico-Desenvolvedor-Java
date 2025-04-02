package com.teste.pratico.application.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CreateReservationRequest {
    @NotNull(message = "O estacionamento é obrigatório!")
    private Long parkingSpotId;

    @NotNull(message = "O usuário é obrigatório!")
    private Long clientId;

    @Future(message = "A data da reserva deve deve ser válida!")
    @NotNull(message = "A data da reserva é obrigatória!")
    private LocalDateTime startTime;
}
