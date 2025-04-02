package com.teste.pratico.application.client;

import com.teste.pratico.application.client.dto.ClientResponse;
import com.teste.pratico.application.client.dto.CreateClientRequest;
import com.teste.pratico.domain.exceptions.ClientAlreadyExistsException;
import com.teste.pratico.domain.entities.Client;
import com.teste.pratico.infrastructure.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateClientUseCase {

    private final ClientRepository clientRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public CreateClientUseCase(ClientRepository clientRepository, BCryptPasswordEncoder passwordEncoder) {
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Client execute(CreateClientRequest createClient) {
        validateUniqueIdentifier(createClient.getIdentifier());

        Client newClient = buildClient(createClient);

        return clientRepository.save(newClient);
    }

    private void validateUniqueIdentifier(String identifier) {
        if (clientRepository.existsByIdentifier(identifier)) {
            throw new ClientAlreadyExistsException("Já existe um usuário com o identificador informado!");
        }
    }

    private Client buildClient(CreateClientRequest request) {
        return new Client(
                request.getIdentifier(),
                request.getName(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword())
        );
    }
}
