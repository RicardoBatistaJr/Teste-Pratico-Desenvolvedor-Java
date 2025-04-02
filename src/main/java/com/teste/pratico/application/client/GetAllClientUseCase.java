package com.teste.pratico.application.client;

import com.teste.pratico.application.client.dto.ClientResponse;
import com.teste.pratico.domain.entities.Client;
import com.teste.pratico.infrastructure.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GetAllClientUseCase {

    private final ClientRepository clientRepository;

    public GetAllClientUseCase(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public List<Client> execute() {
        return clientRepository.findAll();
    }
}

