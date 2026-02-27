package br.com.emprestai.domain.enums.salario;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

public enum FaixaInss {
    Faixa_1_Isencao(BigDecimal.ZERO, new BigDecimal("1621"), new BigDecimal("0.075"), BigDecimal.ZERO),
    Faixa_2_Reduzida(new BigDecimal("1621.01"), new BigDecimal("2902.84"), new BigDecimal("0.09"), new BigDecimal("24.32")),
    Faixa_3_Intermediaria(new BigDecimal("2902.85"), new BigDecimal("4354.27"), new BigDecimal("0.12"), new BigDecimal("92.88")),
    Faixa_4_Superior(new BigDecimal("4354.28"), new BigDecimal("8475.55"), new BigDecimal("0.14"), new BigDecimal("198.50"));

    public final BigDecimal minimo;
    public final BigDecimal maximo;
    public final  BigDecimal aliquota;
    public final BigDecimal parcelaADeduzir;

    FaixaInss(BigDecimal minimo, BigDecimal maximo, BigDecimal aliquota, BigDecimal parcelaADeduzir){
        this.minimo = minimo;
        this.maximo = maximo;
        this.aliquota = aliquota;
        this.parcelaADeduzir = parcelaADeduzir;
    }
    // Metodo que retorna qual a Faixa usar para o calculo
    public static FaixaInss faixaPara(BigDecimal salario){
        return Arrays.stream(values())
                .filter(faixa -> salario.compareTo(faixa.minimo) >= 0
                && salario.compareTo(faixa.maximo) <= 0).findFirst().orElse(Faixa_4_Superior);
    }
    //Metodo para calcular salario com desconto
    public BigDecimal calculaDesconto(BigDecimal salario){
        return salario.multiply(aliquota)
                .subtract(parcelaADeduzir)
                .setScale(2, RoundingMode.HALF_UP);
    }
}
