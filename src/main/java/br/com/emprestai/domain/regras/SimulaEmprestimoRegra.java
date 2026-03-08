package br.com.emprestai.domain.regras;

import br.com.emprestai.domain.entity.simulacao.Simulacao;
import br.com.emprestai.domain.enums.emprestimo.FaixaCredito;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class SimulaEmprestimoRegra {
    public static Simulacao run(BigDecimal salarioLiquido, BigDecimal valorParaSimulacao, Integer parcelas){
        FaixaCredito regra = FaixaCredito.FaixaPara(salarioLiquido);

        // O valor a ser simulado (valorParaSimulacao) agora é definido pelo UseCase.
        // A regra de negócio apenas calcula o valor das parcelas com base nesse valor.
        BigDecimal valorParcelas = regra.calcularParcelas(valorParaSimulacao, parcelas);
        BigDecimal porcentagemSalario = valorParcelas.divide(salarioLiquido, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));

        // O 'limite' no construtor da Simulação de domínio pode ser o valorParaSimulacao.
        return new Simulacao(null, salarioLiquido, regra.name(), valorParaSimulacao, parcelas, valorParcelas, porcentagemSalario);
    }
}
