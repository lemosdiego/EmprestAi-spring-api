package br.com.emprestai.infrastructure.repositories.simulacao;

import br.com.emprestai.infrastructure.entity.simulacao.SimulacaoJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SimulacaoRepository extends JpaRepository<SimulacaoJpa, UUID> { }
