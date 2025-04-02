package com.teste.pratico.application.client;

import com.teste.pratico.domain.entities.Client;
import com.teste.pratico.infrastructure.repositories.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetAllClientUseCaseTest {

    @Mock
    private ClientRepository clientRepository;

    private GetAllClientUseCase getAllClientUseCase;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        getAllClientUseCase = new GetAllClientUseCase(clientRepository);
    }

    @Test
    void shouldReturnAllClients() {
        Client client1 = new Client("123", "John Doe", "john@example.com", "password123");
        Client client2 = new Client("124", "Jane Doe", "jane@example.com", "password456");
        List<Client> expectedClients = Arrays.asList(client1, client2);

        when(clientRepository.findAll()).thenReturn(expectedClients);

        List<Client> clients = getAllClientUseCase.execute();

        assertNotNull(clients);
        assertEquals(2, clients.size());
        assertEquals("John Doe", clients.get(0).getName());
        assertEquals("Jane Doe", clients.get(1).getName());

        verify(clientRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnEmptyListWhenNoClients() {
        when(clientRepository.findAll()).thenReturn(List.of());

        List<Client> clients = getAllClientUseCase.execute();

        assertNotNull(clients);
        assertTrue(clients.isEmpty());

        verify(clientRepository, times(1)).findAll();
    }
}
