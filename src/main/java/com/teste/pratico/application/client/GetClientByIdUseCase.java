package com.teste.pratico.application.client;

import com.teste.pratico.application.client.dto.ClientResponse;
import com.teste.pratico.domain.entities.Client;
import com.teste.pratico.domain.exceptions.ClientNotFoundException;
import com.teste.pratico.infrastructure.repositories.ClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.NotFoundException;

@Service
@Transactional(readOnly = true)
public class GetClientByIdUseCase {

    private final ClientRepository clientRepository;

    public GetClientByIdUseCase(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client execute(Long userId) {
        return clientRepository.findById(userId)
                .orElseThrow(() -> new ClientNotFoundException("Cliente não encontrado!"));
    }
}

