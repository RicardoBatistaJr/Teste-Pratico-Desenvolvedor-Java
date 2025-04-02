package com.teste.pratico.application.reservation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.teste.pratico.application.parkingspot.dto.UpdateSpotRequest;
import com.teste.pratico.application.reservation.dto.CreateReservationRequest;
import com.teste.pratico.application.reservation.usecases.CreateReservationUseCase;
import com.teste.pratico.domain.entities.Client;
import com.teste.pratico.domain.entities.ParkingSpot;
import com.teste.pratico.domain.entities.Reservation;
import com.teste.pratico.domain.enums.ReservationStatusEnum;
import com.teste.pratico.domain.enums.SpotStatusEnum;
import com.teste.pratico.application.parkingspot.usecases.GetAvaibaleSpotUseCase;
import com.teste.pratico.application.parkingspot.usecases.UpdateSpotUseCase;
import com.teste.pratico.application.client.GetClientByIdUseCase;
import com.teste.pratico.domain.enums.SpotTypeEnum;
import com.teste.pratico.infrastructure.repositories.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.ArgumentCaptor;


import java.math.BigDecimal;
import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
class CreateReservationUseCaseTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private GetAvaibaleSpotUseCase getAvaibaleSpotUseCase;

    @Mock
    private GetClientByIdUseCase getClientByIdUseCase;

    @Mock
    private UpdateSpotUseCase updateSpotUseCase;

    @InjectMocks
    private CreateReservationUseCase createReservationUseCase;

    private CreateReservationRequest request;
    private ParkingSpot parkingSpot;
    private Client client;

    @BeforeEach
    void setUp() {
        request = new CreateReservationRequest(1L, 2L, LocalDateTime.now());
        parkingSpot = new ParkingSpot(1L, "COM001", SpotTypeEnum.COMMON, BigDecimal.valueOf(15), SpotStatusEnum.AVAILABLE, null);
        client = new Client(2L, "12345678910", "mockClient","mock@example.com", "mockPassword",null);
    }

    @Test
    void shouldCreateReservationSuccessfully() {
        Reservation expectedReservation = new Reservation(request.getStartTime(), ReservationStatusEnum.OPEN, parkingSpot, client);

        when(getAvaibaleSpotUseCase.execute(request.getParkingSpotId())).thenReturn(parkingSpot);
        when(getClientByIdUseCase.execute(request.getClientId())).thenReturn(client);

        Reservation reservation = createReservationUseCase.execute(request);

        assertNotNull(reservation);
        assertEquals(ReservationStatusEnum.OPEN, reservation.getStatus());
        assertEquals(parkingSpot, reservation.getParkingSpot());
        assertEquals(client, reservation.getClient());

        ArgumentCaptor<Reservation> reservationCaptor = ArgumentCaptor.forClass(Reservation.class);
        verify(reservationRepository).save(reservationCaptor.capture());
        Reservation savedReservation = reservationCaptor.getValue();

        assertEquals(expectedReservation.getStartTime(), savedReservation.getStartTime());
        assertEquals(expectedReservation.getStatus(), savedReservation.getStatus());
        assertEquals(expectedReservation.getParkingSpot().getId(), savedReservation.getParkingSpot().getId());
        assertEquals(expectedReservation.getClient().getId(), savedReservation.getClient().getId());

        verify(updateSpotUseCase, times(1)).execute(eq(parkingSpot.getId()), any(UpdateSpotRequest.class));
    }
}
