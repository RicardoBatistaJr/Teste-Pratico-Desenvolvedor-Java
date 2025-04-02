package com.teste.pratico.application.parkingspot.dto;

import com.teste.pratico.domain.enums.SpotStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UpdateSpotRequest {
    private SpotStatusEnum status;
}
