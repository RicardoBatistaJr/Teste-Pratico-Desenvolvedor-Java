package com.teste.pratico.application.parkingspot.usecases;

import com.teste.pratico.application.parkingspot.dto.ParkingSpotResponse;
import com.teste.pratico.domain.enums.SpotStatusEnum;
import com.teste.pratico.domain.entities.ParkingSpot;
import com.teste.pratico.infrastructure.repositories.ParkingSpotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GetSpotsByStatusUseCase {

    private final ParkingSpotRepository parkingSpotRepository;

    @Autowired
    public GetSpotsByStatusUseCase(ParkingSpotRepository parkingSpotRepository){
        this.parkingSpotRepository = parkingSpotRepository;
    }

    @Transactional(readOnly = true)
    public List<ParkingSpot> execute(SpotStatusEnum status){
        return this.parkingSpotRepository.findByStatus(status);
    }
}
