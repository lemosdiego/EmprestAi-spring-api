package br.com.emprestai.domain.regras;

import br.com.emprestai.domain.entity.cliente.Cliente;
import br.com.emprestai.domain.enums.salario.FaixaInss;

import java.math.BigDecimal;

public class CalculoSalarioLiquidoRegra {
    public Cliente run(Cliente cliente){
        BigDecimal salarioBruto = cliente.getSalarioBruto();
        FaixaInss faixa = FaixaInss.faixaPara(salarioBruto);
        BigDecimal inss = faixa.calculaDesconto(salarioBruto);
        BigDecimal salarioLiquido = salarioBruto.subtract(inss);

        cliente.setSalarioLiquido(salarioLiquido);

        return cliente;
    }
}
