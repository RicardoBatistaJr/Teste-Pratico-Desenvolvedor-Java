package com.teste.pratico.application.parkingspot.dto;

import com.teste.pratico.domain.enums.SpotTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class CreateParkingSpotRequest {
    @NotNull(message = "O código é obrigatório!")
    private String code;

    @NotNull(message = "O tipo da vaga é obrigatório!")
    private SpotTypeEnum type;

    @NotNull(message = "O preço por hora é obrigatório!")
    private BigDecimal pricePerHour;
}
