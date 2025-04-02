package com.teste.pratico.infrastructure.repositories;

import com.teste.pratico.domain.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    boolean existsByIdentifier(String cpf);
}
