package com.teste.pratico.application.parkingspot.dto;

import com.teste.pratico.domain.enums.SpotStatusEnum;
import com.teste.pratico.domain.enums.SpotTypeEnum;
import com.teste.pratico.domain.entities.ParkingSpot;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class ParkingSpotResponse {
    private final Long id;
    private final String code;
    private final SpotTypeEnum type;
    private final BigDecimal pricePerHour;
    private final SpotStatusEnum status;

    public static ParkingSpotResponse from(ParkingSpot spot){
        return new ParkingSpotResponse(spot.getId(), spot.getCode(), spot.getType(), spot.getPricePerHour(), spot.getStatus());
    }
}
