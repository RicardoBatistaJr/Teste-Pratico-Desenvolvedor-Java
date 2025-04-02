package com.teste.pratico.application.client;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.teste.pratico.domain.entities.Client;
import com.teste.pratico.domain.exceptions.ClientNotFoundException;
import com.teste.pratico.infrastructure.repositories.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class GetClientByIdUseCaseTest {

    @Mock
    private ClientRepository clientRepository;

    private GetClientByIdUseCase getClientByIdUseCase;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        getClientByIdUseCase = new GetClientByIdUseCase(clientRepository);
    }

    @Test
    void shouldReturnClientWhenClientExists() {
        String mockIdentifier = "123456";
        String mockName = "John Doe";
        String mockEmail = "john@example.com";
        String mockHashedPassword = "hashedPassword";

        Long clientId = 1L;
        Client mockClient = new Client(mockIdentifier, mockName, mockEmail, mockHashedPassword);
        mockClient.setId(clientId);

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(mockClient));

        Client client = getClientByIdUseCase.execute(clientId);

        assertNotNull(client);
        assertEquals(clientId, client.getId());
        assertEquals(mockName, client.getName());

        verify(clientRepository, times(1)).findById(clientId);
    }

    @Test
    void shouldThrowClientNotFoundExceptionWhenClientDoesNotExist() {
        Long clientId = 1L;

        when(clientRepository.findById(clientId)).thenReturn(Optional.empty());

        ClientNotFoundException exception = assertThrows(ClientNotFoundException.class, () -> {
            getClientByIdUseCase.execute(clientId);
        });

        assertEquals("Cliente não encontrado!", exception.getMessage());

        verify(clientRepository, times(1)).findById(clientId);
    }
}

