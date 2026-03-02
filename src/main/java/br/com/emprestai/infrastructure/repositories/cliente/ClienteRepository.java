package br.com.emprestai.infrastructure.repositories.cliente;

import br.com.emprestai.infrastructure.entity.cliente.ClienteJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ClienteRepository extends JpaRepository<ClienteJpa, UUID> {
}
