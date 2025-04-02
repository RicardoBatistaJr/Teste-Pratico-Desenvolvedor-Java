package com.teste.pratico.infrastructure.repositories;

import com.teste.pratico.domain.entities.ParkingSpot;
import com.teste.pratico.domain.enums.SpotStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParkingSpotRepository extends JpaRepository<ParkingSpot, Long> {
    Optional<ParkingSpot> findByIdAndStatus(Long id, SpotStatusEnum statusEnum);
    boolean existsByCode(String code);
    List<ParkingSpot> findByStatus(SpotStatusEnum status);
}
