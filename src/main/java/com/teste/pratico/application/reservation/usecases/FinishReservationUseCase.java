package com.teste.pratico.application.reservation.usecases;

import com.teste.pratico.application.parkingspot.dto.UpdateSpotRequest;
import com.teste.pratico.application.reservation.dto.FinishReservationResponse;
import com.teste.pratico.domain.entities.ParkingSpot;
import com.teste.pratico.domain.enums.ReservationStatusEnum;
import com.teste.pratico.domain.enums.SpotStatusEnum;
import com.teste.pratico.application.parkingspot.usecases.UpdateSpotUseCase;
import com.teste.pratico.domain.entities.Reservation;
import com.teste.pratico.domain.exceptions.ReservationAlreadyClosedException;
import com.teste.pratico.domain.exceptions.ReservationNotStartedException;
import com.teste.pratico.infrastructure.repositories.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.BadRequestException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class FinishReservationUseCase {

    private static final BigDecimal HUNDRED = BigDecimal.valueOf(100);
    private static final int MINUTES_IN_HOUR = 60;

    private final ReservationRepository reservationRepository;
    private final GetReservationByIdUseCase getReservationByIdUseCase;
    private final UpdateSpotUseCase updateSpotUseCase;

    @Autowired
    public FinishReservationUseCase(ReservationRepository reservationRepository, GetReservationByIdUseCase getReservationByIdUseCase, UpdateSpotUseCase updateSpotUseCase) {
        this.reservationRepository = reservationRepository;
        this.getReservationByIdUseCase = getReservationByIdUseCase;
        this.updateSpotUseCase = updateSpotUseCase;
    }

    @Transactional
    public FinishReservationResponse execute(Long id) {
        Reservation reservation = getReservationByIdUseCase.execute(id);

        validateReservationStatus(reservation);

        LocalDateTime actualTime = LocalDateTime.now();

        long hoursCharged = calculateChargedHours(reservation.getStartTime(), actualTime);
        BigDecimal totalPrice = calculateTotalPrice(reservation.getParkingSpot(), hoursCharged);

        if (actualTime.isBefore(reservation.getStartTime())) {
            hoursCharged = 0;
            totalPrice = BigDecimal.ZERO;
        }

        reservation.setEndTime(actualTime);
        reservation.setStatus(ReservationStatusEnum.FINISHED);
        reservationRepository.save(reservation);
        updateSpotUseCase.execute(reservation.getParkingSpot().getId(), new UpdateSpotRequest(SpotStatusEnum.AVAILABLE));

        return new FinishReservationResponse(
                reservation.getStartTime(),
                actualTime,
                reservation.getParkingSpot().getPricePerHour(),
                hoursCharged,
                reservation.getParkingSpot().getType().getMaintenanceFeeRate(),
                totalPrice
        );
    }

    private void validateReservationStatus(Reservation reservation) {
        if (reservation.getStatus() == ReservationStatusEnum.FINISHED) {
            throw new ReservationAlreadyClosedException("Esta reserva já foi finalizada anteriormente.");
        }
    }

    private long calculateChargedHours(LocalDateTime startTime, LocalDateTime endTime) {
        Duration duration = Duration.between(startTime, endTime);
        return (long) Math.ceil(duration.toMinutes() / (double) MINUTES_IN_HOUR);
    }

    private BigDecimal calculateTotalPrice(ParkingSpot spot, long hoursCharged) {
        BigDecimal maintenanceFeeRate = BigDecimal.valueOf(spot.getType().getMaintenanceFeeRate());
        BigDecimal pricePerHour = spot.getPricePerHour().setScale(2, RoundingMode.HALF_UP);

        return pricePerHour
                .multiply(BigDecimal.valueOf(hoursCharged))
                .multiply(BigDecimal.ONE.add(maintenanceFeeRate.divide(HUNDRED, 2, RoundingMode.HALF_UP)))
                .setScale(2, RoundingMode.HALF_UP);
    }
}

