package com.teste.pratico.domain.entities;

import com.teste.pratico.domain.enums.SpotStatusEnum;
import com.teste.pratico.domain.enums.SpotTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "parking_spots")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParkingSpot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SpotTypeEnum type;

    @Column(nullable = false)
    private BigDecimal pricePerHour;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SpotStatusEnum status;

    @OneToMany(mappedBy = "parkingSpot", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reservation> reservations;

    public ParkingSpot(String code, SpotTypeEnum type, BigDecimal pricePerHour) {
        this.code = code;
        this.type = type;
        this.pricePerHour = pricePerHour;
        this.status = SpotStatusEnum.AVAILABLE;
    }
}