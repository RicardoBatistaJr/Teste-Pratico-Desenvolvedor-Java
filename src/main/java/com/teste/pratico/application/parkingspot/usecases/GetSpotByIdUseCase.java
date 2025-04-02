package com.teste.pratico.application.parkingspot.usecases;

import com.teste.pratico.application.parkingspot.dto.ParkingSpotResponse;
import com.teste.pratico.domain.entities.ParkingSpot;
import com.teste.pratico.domain.exceptions.ParkingSpotNotFoundException;
import com.teste.pratico.infrastructure.repositories.ParkingSpotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.NotFoundException;

@Service
public class GetSpotByIdUseCase {

    private final ParkingSpotRepository parkingSpotRepository;

    @Autowired
    public GetSpotByIdUseCase(ParkingSpotRepository parkingSpotRepository){
        this.parkingSpotRepository = parkingSpotRepository;
    }

    @Transactional(readOnly = true)
    public ParkingSpot execute(Long id){
        return this.parkingSpotRepository.findById(id).orElseThrow(() -> new ParkingSpotNotFoundException("Vaga não encontrada ou indisponível!"));
    }
}
