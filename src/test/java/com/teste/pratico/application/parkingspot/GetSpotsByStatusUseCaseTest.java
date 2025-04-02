package com.teste.pratico.application.parkingspot;

import com.teste.pratico.application.parkingspot.usecases.GetSpotsByStatusUseCase;
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
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetSpotsByStatusUseCaseTest {

    @Mock
    private ParkingSpotRepository parkingSpotRepository;

    @InjectMocks
    private GetSpotsByStatusUseCase getSpotsByStatusUseCase;

    @Test
    void shouldReturnSpotsByStatus() {
        SpotStatusEnum status = SpotStatusEnum.AVAILABLE;
        List<ParkingSpot> spots = Arrays.asList(
                new ParkingSpot("VIP123", SpotTypeEnum.VIP, BigDecimal.valueOf(20.00)),
                new ParkingSpot("VIP124", SpotTypeEnum.VIP, BigDecimal.valueOf(20.00))
        );

        when(parkingSpotRepository.findByStatus(status)).thenReturn(spots);

        List<ParkingSpot> result = getSpotsByStatusUseCase.execute(status);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(status, result.get(0).getStatus());

        ArgumentCaptor<SpotStatusEnum> statusCaptor = ArgumentCaptor.forClass(SpotStatusEnum.class);
        verify(parkingSpotRepository, times(1)).findByStatus(statusCaptor.capture());

        assertEquals(status, statusCaptor.getValue());
    }
}

