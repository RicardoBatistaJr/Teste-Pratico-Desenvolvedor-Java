package com.teste.pratico.application.reservation;

import com.teste.pratico.application.parkingspot.dto.UpdateSpotRequest;
import com.teste.pratico.application.reservation.dto.FinishReservationResponse;
import com.teste.pratico.application.reservation.usecases.FinishReservationUseCase;
import com.teste.pratico.application.reservation.usecases.GetReservationByIdUseCase;
import com.teste.pratico.domain.entities.Client;
import com.teste.pratico.domain.entities.ParkingSpot;
import com.teste.pratico.domain.entities.Reservation;
import com.teste.pratico.domain.enums.ReservationStatusEnum;
import com.teste.pratico.domain.enums.SpotStatusEnum;
import com.teste.pratico.application.parkingspot.usecases.UpdateSpotUseCase;
import com.teste.pratico.domain.enums.SpotTypeEnum;
import com.teste.pratico.domain.exceptions.ReservationAlreadyClosedException;
import com.teste.pratico.domain.exceptions.ReservationNotStartedException;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FinishReservationUseCaseTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private GetReservationByIdUseCase getReservationByIdUseCase;

    @Mock
    private UpdateSpotUseCase updateSpotUseCase;

    @InjectMocks
    private FinishReservationUseCase finishReservationUseCase;

    private ParkingSpot spotReserved;
    private Client client;

    @BeforeEach
    public void setUp(){
        this.spotReserved = new ParkingSpot("VIP123", SpotTypeEnum.VIP, BigDecimal.valueOf(20.00));
        client = new Client("121345678910", "client", "client@example.com", "password");
    }

    @Test
    void shouldFinishReservationSuccessfully() {
        Long reservationId = 1L;
        LocalDateTime fixedTime = LocalDateTime.of(2025, 4, 1, 16, 26, 1, 0);
        Reservation reservation = new Reservation(fixedTime.minusHours(2), ReservationStatusEnum.OPEN, spotReserved, client);
        reservation.setId(reservationId);

        when(getReservationByIdUseCase.execute(reservationId)).thenReturn(reservation);
        when(reservationRepository.save(reservation)).thenReturn(reservation);
        when(updateSpotUseCase.execute(eq(reservation.getParkingSpot().getId()), any(UpdateSpotRequest.class))).thenReturn(null);

        FinishReservationResponse response = finishReservationUseCase.execute(reservationId);

        assertNotNull(response);
        assertEquals(reservation.getStartTime(), response.getStartTime());

        ArgumentCaptor<Reservation> reservationCaptor = ArgumentCaptor.forClass(Reservation.class);
        verify(reservationRepository, times(1)).save(reservationCaptor.capture());

        assertEquals(ReservationStatusEnum.FINISHED, reservationCaptor.getValue().getStatus());

        ArgumentCaptor<UpdateSpotRequest> updateSpotRequestCaptor = ArgumentCaptor.forClass(UpdateSpotRequest.class);
        verify(updateSpotUseCase, times(1)).execute(eq(reservation.getParkingSpot().getId()), updateSpotRequestCaptor.capture());

        assertEquals(SpotStatusEnum.AVAILABLE, updateSpotRequestCaptor.getValue().getStatus());
    }

    @Test
    void shouldReturnHoursAndPriceZeroWhenTimeIsNotSpent() {
        Long reservationId = 1L;
        Reservation reservation = new Reservation(LocalDateTime.now().plusHours(5), ReservationStatusEnum.OPEN, spotReserved, client);
        reservation.setId(reservationId);

        when(getReservationByIdUseCase.execute(reservationId)).thenReturn(reservation);
        when(reservationRepository.save(reservation)).thenReturn(reservation);
        when(updateSpotUseCase.execute(eq(reservation.getParkingSpot().getId()), any(UpdateSpotRequest.class))).thenReturn(null);

        FinishReservationResponse response = finishReservationUseCase.execute(reservationId);

        assertNotNull(response);
        assertEquals(reservation.getStartTime(), response.getStartTime());
        assertEquals(BigDecimal.ZERO, response.getTotalPrice());
        assertEquals(Long.valueOf(0), response.getHoursCharged());

        ArgumentCaptor<Reservation> reservationCaptor = ArgumentCaptor.forClass(Reservation.class);
        verify(reservationRepository, times(1)).save(reservationCaptor.capture());

        assertEquals(ReservationStatusEnum.FINISHED, reservationCaptor.getValue().getStatus());

        ArgumentCaptor<UpdateSpotRequest> updateSpotRequestCaptor = ArgumentCaptor.forClass(UpdateSpotRequest.class);
        verify(updateSpotUseCase, times(1)).execute(eq(reservation.getParkingSpot().getId()), updateSpotRequestCaptor.capture());

        assertEquals(SpotStatusEnum.AVAILABLE, updateSpotRequestCaptor.getValue().getStatus());
    }

    @Test
    void shouldThrowReservationAlreadyClosedExceptionWhenReservationIsFinished() {
        Long reservationId = 1L;
        Reservation reservation = new Reservation(LocalDateTime.now().minusHours(2), ReservationStatusEnum.FINISHED, spotReserved, client);
        reservation.setId(reservationId);

        when(getReservationByIdUseCase.execute(reservationId)).thenReturn(reservation);

        assertThrows(ReservationAlreadyClosedException.class, () -> finishReservationUseCase.execute(reservationId));

        verify(reservationRepository, never()).save(any(Reservation.class));
        verify(updateSpotUseCase, never()).execute(anyLong(), any(UpdateSpotRequest.class));
    }

}
