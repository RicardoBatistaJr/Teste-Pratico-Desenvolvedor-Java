package com.teste.pratico.domain.entities;

import com.teste.pratico.domain.enums.ReservationStatusEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    ReservationStatusEnum status;

    @ManyToOne
    @JoinColumn(name = "parking_spot_id", nullable = false)
    private ParkingSpot parkingSpot;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    public Reservation (LocalDateTime startTime, ReservationStatusEnum reservationStatus, ParkingSpot parkingSpot, Client client){
        this.startTime = startTime;
        this.status = reservationStatus;
        this.parkingSpot = parkingSpot;
        this.client = client;
    }
}
