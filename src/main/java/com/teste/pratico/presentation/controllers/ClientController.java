package com.teste.pratico.presentation.controllers;

import com.teste.pratico.application.client.dto.CreateClientRequest;
import com.teste.pratico.application.client.dto.ClientResponse;
import com.teste.pratico.application.client.CreateClientUseCase;
import com.teste.pratico.application.client.GetAllClientUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/clients")
public class ClientController {
    private final CreateClientUseCase createClientUseCase;
    private final GetAllClientUseCase getAllClientUseCase;

    public ClientController(CreateClientUseCase createClientUseCase, GetAllClientUseCase getAllClientUseCase) {
        this.createClientUseCase = createClientUseCase;
        this.getAllClientUseCase = getAllClientUseCase;
    }

    @Operation(
            summary = "Cadastro de cliente.",
            description = "Cadastra um novo cliente no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna o cliente cadastrado."),
            @ApiResponse(responseCode = "409", description = "Usuário cadastrado com os dados informados." ),
            @ApiResponse(responseCode = "400", description = "Informa que algum dos campos está inválido." )}
    )
    @PostMapping
    public ResponseEntity<ClientResponse> createClient(@RequestBody @Validated CreateClientRequest request) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(ClientResponse.from(createClientUseCase.execute(request)));
    }

    @Operation(
            summary = "Listagem de clientes.",
            description = "Retorna todos os clientes cadastrados no sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna a lista de clientes cadastrados."),
            @ApiResponse(responseCode = "204", description = "Informa que não há clientes cadastrados." )}
    )
    @GetMapping
    public ResponseEntity<List<ClientResponse>> getAllClients(){
        List<ClientResponse> clients = getAllClientUseCase.execute().stream()
                .map(ClientResponse::from)
                .collect(Collectors.toList());

        if (clients.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(clients);
    }
}
