package com.teste.pratico.application.parkingspot.usecases;

import com.teste.pratico.application.parkingspot.dto.ParkingSpotResponse;
import com.teste.pratico.application.parkingspot.dto.UpdateSpotRequest;
import com.teste.pratico.domain.entities.ParkingSpot;
import com.teste.pratico.infrastructure.repositories.ParkingSpotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UpdateSpotUseCase {
    private final ParkingSpotRepository parkingSpotRepository;
    private final GetSpotByIdUseCase getSpotByIdUseCase;

    @Autowired
    public UpdateSpotUseCase(ParkingSpotRepository parkingSpotRepository, GetSpotByIdUseCase getSpotByIdUseCase){
        this.parkingSpotRepository = parkingSpotRepository;
        this.getSpotByIdUseCase = getSpotByIdUseCase;
    }

    @Transactional
    public ParkingSpot execute(Long id, UpdateSpotRequest request) {
        ParkingSpot parkingSpot = this.getSpotByIdUseCase.execute(id);
        parkingSpot.setStatus(request.getStatus());

        return parkingSpotRepository.save(parkingSpot);
    }
}
