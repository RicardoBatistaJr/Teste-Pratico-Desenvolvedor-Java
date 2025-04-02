package com.teste.pratico.presentation.controllers;

import com.teste.pratico.application.parkingspot.dto.CreateParkingSpotRequest;
import com.teste.pratico.application.parkingspot.dto.ParkingSpotResponse;
import com.teste.pratico.domain.enums.SpotStatusEnum;
import com.teste.pratico.domain.enums.SpotTypeEnum;
import com.teste.pratico.application.parkingspot.usecases.CreateSpotUseCase;
import com.teste.pratico.application.parkingspot.usecases.GetSpotsByStatusUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/spots")
public class ParkingSpotController {
    private final CreateSpotUseCase createSpotUseCase;
    private final GetSpotsByStatusUseCase getSpotsByStatusUseCase;

    public ParkingSpotController(
            CreateSpotUseCase createSpotUseCase,
            GetSpotsByStatusUseCase getSpotsByStatusUseCase
    ) {
        this.createSpotUseCase = createSpotUseCase;
        this.getSpotsByStatusUseCase = getSpotsByStatusUseCase;
    }

    @Operation(
            summary = "Cadastro de vaga de estacionamento.",
            description = "Cria uma nova vaga de estacionamento no sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Vaga de estacionamento criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Algum dos campos inseridos está inválido."),
            @ApiResponse(responseCode = "409", description = "Vaga já cadastrada com os dados informados."),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping
    public ResponseEntity<ParkingSpotResponse> createSpot(@RequestBody @Validated CreateParkingSpotRequest request) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(ParkingSpotResponse.from(this.createSpotUseCase.execute(request)));
    }

    @Operation(summary = "Listagem de vagas disponíveis.", description = "Retorna todos as vagas de estacionamento que estão com o status disponível no sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna a lista de vagas de estacionamento cadastradas."),
            @ApiResponse(responseCode = "204", description = "Informa que não há vagas de estacionamento cadastradas." )}
    )
    @GetMapping("/available")
    public ResponseEntity<List<ParkingSpotResponse>> getAvailableSpots() {
        List<ParkingSpotResponse> spots = this.getSpotsByStatusUseCase.execute(SpotStatusEnum.AVAILABLE)
                .stream()
                .map(ParkingSpotResponse::from)
                .collect(Collectors.toList());

        if (spots.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(spots);
    }

}
