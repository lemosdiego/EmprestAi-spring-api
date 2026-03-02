package br.com.emprestai.domain.entity.cliente;

import java.math.BigDecimal;
import java.util.UUID;

public class Cliente {
    private UUID id;
    private String nome;
    private BigDecimal salarioBruto;
    private BigDecimal salatioLiquido;

    public Cliente(String nome, BigDecimal salarioBruto, BigDecimal salatioLiquido){
        this.nome = this.nome;
        this.salarioBruto = salarioBruto;
        this.salatioLiquido = salatioLiquido;
    }
    public String getNome(){return nome;}
    public BigDecimal getSalarioBruto(){return salarioBruto;}
    public BigDecimal getSalatioLiquido(){return salatioLiquido;}
    public void setSalarioLiquido(BigDecimal salatioLiquido) {
        this.salatioLiquido = salatioLiquido;
    }
}
