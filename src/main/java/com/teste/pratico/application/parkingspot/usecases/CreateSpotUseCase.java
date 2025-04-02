package com.teste.pratico.application.parkingspot.usecases;

import com.teste.pratico.application.parkingspot.dto.CreateParkingSpotRequest;
import com.teste.pratico.application.parkingspot.dto.ParkingSpotResponse;
import com.teste.pratico.domain.entities.ParkingSpot;
import com.teste.pratico.domain.enums.SpotTypeEnum;
import com.teste.pratico.domain.exceptions.ParkingSpotAlreadyExistsException;
import com.teste.pratico.domain.exceptions.ParkingSpotNotFoundException;
import com.teste.pratico.infrastructure.repositories.ParkingSpotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class CreateSpotUseCase {

    private final ParkingSpotRepository parkingSpotRepository;

    @Autowired
    public CreateSpotUseCase(ParkingSpotRepository parkingSpotRepository){
        this.parkingSpotRepository = parkingSpotRepository;
    }

    @Transactional
    public ParkingSpot execute(CreateParkingSpotRequest request) {
        String spotCode = request.getType() == SpotTypeEnum.COMMON ? ("COM"+request.getCode()) : ("VIP"+request.getCode());

        if(parkingSpotRepository.existsByCode(spotCode)){
            throw new ParkingSpotAlreadyExistsException("Já existe uma vaga com o código informado");
        }

        ParkingSpot spot = new ParkingSpot(spotCode,request.getType(),request.getPricePerHour());

        return parkingSpotRepository.save(spot);
    };
}
