package com.teste.pratico.application.parkingspot;

import com.teste.pratico.application.parkingspot.dto.UpdateSpotRequest;
import com.teste.pratico.application.parkingspot.usecases.GetSpotByIdUseCase;
import com.teste.pratico.application.parkingspot.usecases.GetSpotsByStatusUseCase;
import com.teste.pratico.application.parkingspot.usecases.UpdateSpotUseCase;
import com.teste.pratico.domain.entities.ParkingSpot;
import com.teste.pratico.domain.enums.SpotStatusEnum;
import com.teste.pratico.domain.enums.SpotTypeEnum;
import com.teste.pratico.infrastructure.repositories.ParkingSpotRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UpdateSpotUseCaseTest {

    @Mock
    private ParkingSpotRepository parkingSpotRepository;

    @Mock
    private GetSpotByIdUseCase getSpotByIdUseCase;

    @InjectMocks
    private UpdateSpotUseCase updateSpotUseCase;

    @Test
    void shouldUpdateParkingSpotStatusSuccessfully() {
        Long spotId = 1L;
        UpdateSpotRequest request = new UpdateSpotRequest(SpotStatusEnum.RESERVED);
        ParkingSpot parkingSpot = new ParkingSpot("VIP123", SpotTypeEnum.VIP, BigDecimal.valueOf(20.00));
        parkingSpot.setId(spotId);

        when(getSpotByIdUseCase.execute(spotId)).thenReturn(parkingSpot);
        when(parkingSpotRepository.save(parkingSpot)).thenReturn(parkingSpot);

        ParkingSpot result = updateSpotUseCase.execute(spotId, request);

        assertNotNull(result);
        assertEquals(SpotStatusEnum.RESERVED, result.getStatus());

        ArgumentCaptor<ParkingSpot> parkingSpotCaptor = ArgumentCaptor.forClass(ParkingSpot.class);
        verify(parkingSpotRepository, times(1)).save(parkingSpotCaptor.capture());

        assertEquals(SpotStatusEnum.RESERVED, parkingSpotCaptor.getValue().getStatus());
    }
}

