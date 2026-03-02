package br.com.emprestai.domain.regras;

import br.com.emprestai.domain.entity.simulacao.Simulacao;
import br.com.emprestai.domain.enums.emprestimo.FaixaCredito;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class SimulaEmprestimoRegra {
    public static Simulacao run(BigDecimal salarioLiquido, Integer parcelas){
        FaixaCredito regra = FaixaCredito.FaixaPara(salarioLiquido);

        BigDecimal limite = regra.calcularLimite(salarioLiquido);
        BigDecimal valorParcelas = regra.calcularParcelas(limite, parcelas);
        BigDecimal porcentagemSalario = valorParcelas.divide(salarioLiquido, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));

        return new Simulacao(null, salarioLiquido, regra.name(), limite, parcelas, valorParcelas, porcentagemSalario);
    }
}
