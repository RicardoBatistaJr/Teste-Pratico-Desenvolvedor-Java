package com.teste.pratico.application.parkingspot;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.teste.pratico.application.parkingspot.usecases.GetSpotByIdUseCase;
import com.teste.pratico.domain.entities.ParkingSpot;
import com.teste.pratico.domain.enums.SpotTypeEnum;
import com.teste.pratico.infrastructure.repositories.ParkingSpotRepository;
import com.teste.pratico.domain.exceptions.ParkingSpotNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class GetSpotByIdUseCaseTest {

    @Mock
    private ParkingSpotRepository parkingSpotRepository;

    @InjectMocks
    private GetSpotByIdUseCase getSpotByIdUseCase;

    private ParkingSpot parkingSpot;

    @BeforeEach
    public void setUp() {
        parkingSpot = new ParkingSpot("VIP123", SpotTypeEnum.VIP, new BigDecimal("20.00"));
    }

    @Test
    void shouldReturnParkingSpotWhenFound() {
        Long spotId = 1L;

        when(parkingSpotRepository.findById(spotId)).thenReturn(Optional.of(parkingSpot));

        ParkingSpot response = getSpotByIdUseCase.execute(spotId);

        assertNotNull(response);
        assertEquals(parkingSpot.getId(), response.getId());
        assertEquals(parkingSpot.getCode(), response.getCode());

        ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        verify(parkingSpotRepository, times(1)).findById(captor.capture());

        assertEquals(spotId, captor.getValue());
    }

    @Test
    void shouldThrowExceptionWhenParkingSpotNotFound() {
        Long spotId = 999L;

        when(parkingSpotRepository.findById(spotId)).thenReturn(Optional.empty());

        ParkingSpotNotFoundException exception = assertThrows(ParkingSpotNotFoundException.class, () -> {
            getSpotByIdUseCase.execute(spotId);
        });

        assertEquals("Vaga não encontrada ou indisponível!", exception.getMessage());

        ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        verify(parkingSpotRepository, times(1)).findById(captor.capture());
        assertEquals(spotId, captor.getValue());
    }
}
