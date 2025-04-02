package com.teste.pratico.application.reservation.usecases;

import com.teste.pratico.domain.entities.Reservation;
import com.teste.pratico.domain.exceptions.ReservationNotFoundException;
import com.teste.pratico.infrastructure.repositories.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.NotFoundException;

@Service
public class GetReservationByIdUseCase {

    private final ReservationRepository reservationRepository;

    @Autowired
    public GetReservationByIdUseCase(ReservationRepository reservationRepository){
        this.reservationRepository = reservationRepository;
    }

    public Reservation execute(Long id) {
        return this.reservationRepository.findById(id).orElseThrow(() -> new ReservationNotFoundException("Reserva não encontrada!"));
    }
}
