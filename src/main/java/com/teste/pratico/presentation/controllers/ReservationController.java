package com.teste.pratico.presentation.controllers;

import com.teste.pratico.application.reservation.dto.CreateReservationRequest;
import com.teste.pratico.application.reservation.dto.FinishReservationResponse;
import com.teste.pratico.application.reservation.dto.ReservationResponse;
import com.teste.pratico.application.reservation.usecases.CreateReservationUseCase;
import com.teste.pratico.application.reservation.usecases.FinishReservationUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final CreateReservationUseCase createReservationUseCase;
    private final FinishReservationUseCase finishReservationUseCase;

    @Autowired
    public ReservationController(CreateReservationUseCase createReservationUseCase, FinishReservationUseCase finishReservationUseCase){
        this.createReservationUseCase = createReservationUseCase;
        this.finishReservationUseCase = finishReservationUseCase;
    }

    @Operation(
            summary = "Cadastro de reserva.",
            description = "Cria uma reserva para uma vaga de estacionamente específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna a reserva cadastrada."),
            @ApiResponse(responseCode = "404", description = "Usuário informado não encontrado." ),
            @ApiResponse(responseCode = "404", description = "Vaga de estacionamento informada não encontrada." ),
            @ApiResponse(responseCode = "400", description = "Informa que algum dos campos está inválido." )}
    )
    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(@RequestBody @Validated CreateReservationRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(ReservationResponse.from(this.createReservationUseCase.execute(request)));
    }

    @Operation(
            summary = "Finalização de reserva.",
            description = "Finaliza uma reserva calculando os custos para o cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna os valores da reserva."),
            @ApiResponse(responseCode = "404", description = "Reserva informada não encontrada." ),
            @ApiResponse(responseCode = "409", description = "Reserva informada já está finalizada." ),
            @ApiResponse(responseCode = "400", description = "Informa que algum dos campos está inválido." )}
    )
    @PatchMapping("/{id}/finish")
    public ResponseEntity<FinishReservationResponse> finishReservation(@PathVariable Long id){
        FinishReservationResponse response = this.finishReservationUseCase.execute(id);
        return ResponseEntity.ok(response);
    }
}
