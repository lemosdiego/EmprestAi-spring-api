package br.com.emprestai.infrastructure.entity.simulacao;

import br.com.emprestai.infrastructure.entity.cliente.ClienteJpa;
import jakarta.persistence.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "simulacoes")
@EntityListeners(AuditingEntityListener.class)
public class SimulacaoJpa {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "cliente_id", nullable = false)
    private UUID clienteId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", insertable = false, updatable = false)
    private ClienteJpa cliente;

    @Column(name = "valor_solicitado", nullable = false, precision = 12, scale = 2)
    private BigDecimal valorSolicitado;

    @Column(name = "prazo_meses", nullable = false)
    private Integer prazoMeses;

    @Column(name = "score")
    private Integer score;

    @Column(name = "resultado", nullable = false)
    private String resultado;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    protected SimulacaoJpa(){}

    public SimulacaoJpa(
        UUID clienteId,
        BigDecimal valorSolicitado,
        Integer prazoMeses,
        String resultado
    ){
        this.clienteId = clienteId;
        this.valorSolicitado = valorSolicitado;
        this.prazoMeses = prazoMeses;
        this.resultado = resultado;
        this.createdAt = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getClienteId() {
        return clienteId;
    }

    public void setClienteId(UUID clienteId) {
        this.clienteId = clienteId;
    }

    public ClienteJpa getCliente() {
        return cliente;
    }

    public void setCliente(ClienteJpa cliente) {
        this.cliente = cliente;
    }

    public BigDecimal getValorSolicitado() {
        return valorSolicitado;
    }

    public void setValorSolicitado(BigDecimal valorSolicitado) {
        this.valorSolicitado = valorSolicitado;
    }

    public Integer getPrazoMeses() {
        return prazoMeses;
    }

    public void setPrazoMeses(Integer prazoMeses) {
        this.prazoMeses = prazoMeses;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
