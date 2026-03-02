package br.com.emprestai.application.usecases;


import br.com.emprestai.domain.entity.cliente.Cliente;
import br.com.emprestai.domain.entity.simulacao.Simulacao;
import br.com.emprestai.domain.enums.emprestimo.FaixaCredito;
import br.com.emprestai.domain.regras.CalculoSalarioLiquidoRegra;
import br.com.emprestai.domain.regras.SimulaEmprestimoRegra;

import java.math.BigDecimal;
import java.util.List;

public class SimularEmprestimoUseCase {
    public void run( ){
        Cliente cliente = new Cliente(new BigDecimal("1621"), BigDecimal.ZERO);
        CalculoSalarioLiquidoRegra salarioLiquidoRegra = new CalculoSalarioLiquidoRegra();
        Cliente calcularSalarioLiquidoCliente = salarioLiquidoRegra.run(cliente);
        BigDecimal salarioLiquido = calcularSalarioLiquidoCliente.getSalatioLiquido();
        System.out.println("Salario liquido: R$ " + salarioLiquido);

        SimulaEmprestimoRegra emprestimoRegra = new SimulaEmprestimoRegra();
        FaixaCredito faixa = FaixaCredito.FaixaPara(salarioLiquido);
        System.out.println("\nFaixa de credito: " + faixa.name());
        System.out.println("Limite liberado de: " + faixa.calcularLimite(salarioLiquido));

        List<Integer> opcoes = faixa.getOpcoesDeParcelamento();
        System.out.println("Confira nossos planos: " + opcoes);

        for (Integer parcelas: opcoes){
            Simulacao simulacao = SimulaEmprestimoRegra.run(salarioLiquido, parcelas);
            System.out.printf("• %d meses de R$%.2f", parcelas, simulacao.valorDasParcelas);
            System.out.printf(" (juros %.1f%% ao mês., comprometimento %.1f%% salário)\n",
                    faixa.taxaDeJuro,
                    simulacao.porcentagemDoSalario);

        }
    }
}
