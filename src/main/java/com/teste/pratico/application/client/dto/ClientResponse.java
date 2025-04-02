package com.teste.pratico.application.client.dto;

import com.teste.pratico.domain.entities.Client;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ClientResponse {
    private Long id;
    private String identifier;
    private String name;
    private String email;

    public static ClientResponse from(Client client) {
        return new ClientResponse(client.getId(), client.getIdentifier(), client.getName(), client.getEmail());
    }
}
