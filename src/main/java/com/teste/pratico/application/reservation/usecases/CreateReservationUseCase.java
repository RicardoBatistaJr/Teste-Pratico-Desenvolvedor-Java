package com.teste.pratico.application.reservation.usecases;

import com.teste.pratico.application.parkingspot.dto.UpdateSpotRequest;
import com.teste.pratico.domain.entities.Reservation;
import com.teste.pratico.domain.entities.ParkingSpot;
import com.teste.pratico.domain.enums.ReservationStatusEnum;
import com.teste.pratico.domain.enums.SpotStatusEnum;
import com.teste.pratico.application.parkingspot.usecases.GetAvaibaleSpotUseCase;
import com.teste.pratico.application.parkingspot.usecases.UpdateSpotUseCase;
import com.teste.pratico.application.reservation.dto.CreateReservationRequest;
import com.teste.pratico.domain.entities.Client;
import com.teste.pratico.application.client.GetClientByIdUseCase;
import com.teste.pratico.infrastructure.repositories.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateReservationUseCase {

    private final ReservationRepository reservationRepository;
    private final GetAvaibaleSpotUseCase getAvaibaleSpotUseCase;
    private final GetClientByIdUseCase getClientByIdUseCase;
    private final UpdateSpotUseCase updateSpotUseCase;

    @Autowired
    public CreateReservationUseCase(ReservationRepository reservationRepository, GetAvaibaleSpotUseCase getAvaibaleSpotUseCase, GetClientByIdUseCase getClientByIdUseCase, UpdateSpotUseCase updateSpotUseCase){
        this.reservationRepository = reservationRepository;
        this.getAvaibaleSpotUseCase = getAvaibaleSpotUseCase;
        this.getClientByIdUseCase = getClientByIdUseCase;
        this.updateSpotUseCase = updateSpotUseCase;
    }

    @Transactional
    public Reservation execute(CreateReservationRequest request){
        ParkingSpot spot = this.getAvaibaleSpotUseCase.execute(request.getParkingSpotId());
        Client client = this.getClientByIdUseCase.execute(request.getClientId());

        Reservation reservation = new Reservation(request.getStartTime(), ReservationStatusEnum.OPEN, spot, client);

        reservationRepository.save(reservation);
        updateSpotUseCase.execute(spot.getId(), new UpdateSpotRequest(SpotStatusEnum.RESERVED));

        return reservation;
    }
}
