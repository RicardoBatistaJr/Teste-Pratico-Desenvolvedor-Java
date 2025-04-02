package com.teste.pratico.application.client;

import com.teste.pratico.application.client.dto.CreateClientRequest;
import com.teste.pratico.domain.entities.Client;
import com.teste.pratico.domain.exceptions.ClientAlreadyExistsException;
import com.teste.pratico.infrastructure.repositories.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateClientUseCaseTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private CreateClientUseCase createClientUseCase;

    CreateClientRequest request;

    @BeforeEach
    void setUp() {
        createClientUseCase = new CreateClientUseCase(clientRepository, passwordEncoder);
        request = new CreateClientRequest("123456", "John Doe", "john@example.com", "password123");
    }

    @Test
    void shouldCreateClientSuccessfully() {
        String mockIdentifier = "123456";
        String mockName = "John Doe";
        String mockEmail = "john@example.com";
        String mockHashedPassword = "hashedPassword";

        Client mockClient = new Client(mockIdentifier, mockName, mockEmail, mockHashedPassword);

        when(clientRepository.existsByIdentifier(request.getIdentifier())).thenReturn(false);
        when(passwordEncoder.encode(request.getPassword())).thenReturn(mockHashedPassword);
        when(clientRepository.save(any(Client.class))).thenReturn(mockClient);

        Client response = createClientUseCase.execute(request);

        assertNotNull(response);
        assertEquals(mockIdentifier, response.getIdentifier());
        assertEquals(mockName, response.getName());
        assertEquals(mockEmail, response.getEmail());

        verify(clientRepository, times(1)).existsByIdentifier(request.getIdentifier());
        verify(passwordEncoder, times(1)).encode(request.getPassword());

        ArgumentCaptor<Client> captor = ArgumentCaptor.forClass(Client.class);
        verify(clientRepository, times(1)).save(captor.capture());

        Client capturedClient = captor.getValue();

        assertEquals(mockHashedPassword, capturedClient.getPassword());
    }

    @Test
    void shouldThrowExceptionWhenClientAlreadyExists() {
        when(clientRepository.existsByIdentifier(request.getIdentifier())).thenReturn(true);

        assertThrows(ClientAlreadyExistsException.class, () -> createClientUseCase.execute(request));

        verify(clientRepository, times(1)).existsByIdentifier(request.getIdentifier());
        verify(clientRepository, never()).save(any(Client.class));
    }
}
