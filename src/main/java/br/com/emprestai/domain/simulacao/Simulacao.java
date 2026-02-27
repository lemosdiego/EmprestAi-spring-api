package br.com.emprestai.domain.simulacao;

import java.math.BigDecimal;
import java.util.UUID;

public class Simulacao {
    UUID id;
    BigDecimal salarioLiquido;
    public String faixaDeCredito;
    public BigDecimal limiteTotal;
    public Integer parcelas;
    public BigDecimal valorDasParcelas;
    public BigDecimal porcentagemDoSalario;

    public Simulacao (UUID id, BigDecimal salarioLiquido, String faixaDeCredito, BigDecimal limiteTotal, Integer parcelas, BigDecimal valorDasParcelas){
        this.salarioLiquido = salarioLiquido;
        this.faixaDeCredito = faixaDeCredito;
        this.limiteTotal = limiteTotal;
        this.parcelas = parcelas;
        this.valorDasParcelas = valorDasParcelas;
        this.porcentagemDoSalario = valorDasParcelas;
    }
}
