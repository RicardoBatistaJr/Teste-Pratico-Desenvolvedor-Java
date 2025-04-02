package com.teste.pratico.application.parkingspot.usecases;

import com.teste.pratico.application.parkingspot.dto.ParkingSpotResponse;
import com.teste.pratico.domain.enums.SpotStatusEnum;
import com.teste.pratico.domain.entities.ParkingSpot;
import com.teste.pratico.domain.exceptions.ParkingSpotNotFoundException;
import com.teste.pratico.infrastructure.repositories.ParkingSpotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.NotFoundException;

@Service
public class GetAvaibaleSpotUseCase {

    private final ParkingSpotRepository parkingSpotRepository;

    @Autowired
    public GetAvaibaleSpotUseCase(ParkingSpotRepository parkingSpotRepository){
        this.parkingSpotRepository = parkingSpotRepository;
    }

    @Transactional(readOnly = true)
    public ParkingSpot execute(Long id) {
        return parkingSpotRepository.findByIdAndStatus(id, SpotStatusEnum.AVAILABLE)
                .orElseThrow(() -> new ParkingSpotNotFoundException("Vaga não encontrada ou indisponível!"));
    }
}
