package com.teste.pratico.application.parkingspot;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.teste.pratico.application.parkingspot.usecases.GetAvaibaleSpotUseCase;
import com.teste.pratico.domain.entities.ParkingSpot;
import com.teste.pratico.domain.enums.SpotStatusEnum;
import com.teste.pratico.domain.enums.SpotTypeEnum;
import com.teste.pratico.domain.exceptions.ParkingSpotNotFoundException;
import com.teste.pratico.infrastructure.repositories.ParkingSpotRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class GetAvailableSpotUseCaseTest {

    @Mock
    private ParkingSpotRepository mockParkingSpotRepository;

    @InjectMocks
    private GetAvaibaleSpotUseCase getAvailableSpotUseCase;

    @Test
    void shouldReturnParkingSpotWhenAvailable() {
        Long id = 1L;
        ParkingSpot mockSpot = new ParkingSpot("VIP123", SpotTypeEnum.VIP, new BigDecimal("20.00"));

        when(mockParkingSpotRepository.findByIdAndStatus(id, SpotStatusEnum.AVAILABLE)).thenReturn(Optional.of(mockSpot));

        ParkingSpot response = getAvailableSpotUseCase.execute(id);

        assertNotNull(response);
        assertEquals(mockSpot.getCode(), response.getCode());
        verify(mockParkingSpotRepository, times(1)).findByIdAndStatus(id, SpotStatusEnum.AVAILABLE);
    }

    @Test
    void shouldThrowParkingSpotNotFoundExceptionWhenSpotNotAvailable() {
        Long id = 1L;

        when(mockParkingSpotRepository.findByIdAndStatus(id, SpotStatusEnum.AVAILABLE)).thenReturn(Optional.empty());

        ParkingSpotNotFoundException exception = assertThrows(ParkingSpotNotFoundException.class, () -> {
            getAvailableSpotUseCase.execute(id);
        });

        assertEquals("Vaga não encontrada ou indisponível!", exception.getMessage());
        verify(mockParkingSpotRepository, times(1)).findByIdAndStatus(id, SpotStatusEnum.AVAILABLE);
    }
}
