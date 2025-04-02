package com.teste.pratico.application.parkingspot;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.teste.pratico.application.parkingspot.dto.CreateParkingSpotRequest;
import com.teste.pratico.application.parkingspot.usecases.CreateSpotUseCase;
import com.teste.pratico.domain.entities.ParkingSpot;
import com.teste.pratico.domain.enums.SpotTypeEnum;
import com.teste.pratico.domain.exceptions.ParkingSpotAlreadyExistsException;
import com.teste.pratico.infrastructure.repositories.ParkingSpotRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

@ExtendWith(MockitoExtension.class)
public class CreateSpotUseCaseTest {

    @Mock
    private ParkingSpotRepository parkingSpotRepository;

    @InjectMocks
    private CreateSpotUseCase createSpotUseCase;

    private CreateParkingSpotRequest request;

    @BeforeEach
    public void setUp() {
        request = new CreateParkingSpotRequest("123", SpotTypeEnum.VIP,new BigDecimal("20.00"));
    }

    @Test
    void shouldCreateParkingSpotSuccessfully() {
        String spotCode = "VIP123";

        when(parkingSpotRepository.existsByCode(spotCode)).thenReturn(false);

        createSpotUseCase.execute(request);

        ArgumentCaptor<ParkingSpot> captor = ArgumentCaptor.forClass(ParkingSpot.class);
        verify(parkingSpotRepository).save(captor.capture());

        ParkingSpot capturedSpot = captor.getValue();
        assertEquals(spotCode, capturedSpot.getCode());
        assertEquals(request.getType(), capturedSpot.getType());
        assertEquals(request.getPricePerHour(), capturedSpot.getPricePerHour());

        verify(parkingSpotRepository, times(1)).existsByCode(spotCode);
        verify(parkingSpotRepository, times(1)).save(any(ParkingSpot.class));
    }

    @Test
    void shouldThrowParkingSpotAlreadyExistsExceptionWhenSpotExists() {
        String mockSpotCode = "VIP123";

        when(parkingSpotRepository.existsByCode(mockSpotCode)).thenReturn(true);

        ParkingSpotAlreadyExistsException exception = assertThrows(ParkingSpotAlreadyExistsException.class, () -> {
            createSpotUseCase.execute(request);
        });

        assertEquals("Já existe uma vaga com o código informado", exception.getMessage());
        verify(parkingSpotRepository, times(1)).existsByCode(mockSpotCode);
        verify(parkingSpotRepository, times(0)).save(any(ParkingSpot.class));
    }
}
