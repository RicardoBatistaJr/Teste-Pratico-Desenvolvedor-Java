package com.teste.pratico.application.reservation;

import com.teste.pratico.application.reservation.usecases.GetReservationByIdUseCase;
import com.teste.pratico.domain.entities.Reservation;
import com.teste.pratico.domain.exceptions.ReservationNotFoundException;
import com.teste.pratico.infrastructure.repositories.ReservationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.ArgumentCaptor;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetReservationByIdUseCaseTest {

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private GetReservationByIdUseCase getReservationByIdUseCase;

    @Captor
    private ArgumentCaptor<Long> idCaptor;

    @Test
    void shouldReturnReservationWhenFound() {
        Reservation reservation = new Reservation();
        reservation.setId(1L);

        when(reservationRepository.findById(anyLong())).thenReturn(Optional.of(reservation));

        Reservation result = getReservationByIdUseCase.execute(1L);

        assertEquals(reservation, result);
        verify(reservationRepository).findById(idCaptor.capture());
        assertEquals(1L, idCaptor.getValue());
    }

    @Test
    void shouldThrowReservationNotFoundExceptionWhenNotFound() {
        when(reservationRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ReservationNotFoundException.class, () -> {
            getReservationByIdUseCase.execute(1L);
        });

        verify(reservationRepository).findById(idCaptor.capture());
        assertEquals(1L, idCaptor.getValue());
    }
}
