package br.com.emprestai.domain.enums.emprestimo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum FaixaCredito {
    Renda_inicial(new BigDecimal("1450"), new BigDecimal("2500"), new BigDecimal("3.0"),12,18, new BigDecimal("1.8"), new BigDecimal("30") ),
    Renda_media(new BigDecimal("2501.00"), new BigDecimal("4500.00"), new BigDecimal("5.0"), 12, 24, new BigDecimal("1.5"), new BigDecimal("25")),
    Renda_alta(new BigDecimal("4501.00"), new BigDecimal("7500.00"), new BigDecimal("7.0"), 12, 30, new BigDecimal("1.2"), new BigDecimal("20")),
    Renda_premium(new BigDecimal("7501.00"), new BigDecimal("999999.99"), new BigDecimal("8.0"), 12, 60, new BigDecimal("1.0"), new BigDecimal("15"));

    public final BigDecimal minimo;
    public final BigDecimal maximo;
    public final BigDecimal multiplicador;
    public final int parcelasMinimas;
    public final int parcelasMaximas;
    public final BigDecimal taxaDeJuro;
    public final BigDecimal porcentagemDoSalario;

    FaixaCredito(BigDecimal minimo, BigDecimal maximo, BigDecimal multiplicador, int parcelasMinimas, int parcelasMaximas, BigDecimal taxaDeJuro, BigDecimal porcentagemDoSalario){
        this.minimo = minimo;
        this.maximo = maximo;
        this.multiplicador = multiplicador;
        this.parcelasMinimas = parcelasMinimas;
        this.parcelasMaximas = parcelasMaximas;
        this.taxaDeJuro = taxaDeJuro;
        this.porcentagemDoSalario = porcentagemDoSalario;
    }
    //Metodo responsavel por verificar para qual faixa de credito o salario liquido se encaixa;
    //Retorna um array e usamos o filter para saber em qual faixa o salario se encaixa, se nao encaixar entao renda premium
    public static FaixaCredito FaixaPara(BigDecimal salarioLiquido){
        if(salarioLiquido.compareTo(Renda_inicial.minimo) < 0){
            System.out.println("Salario abaixo do minimo permitido");
        }
        return Arrays.stream(values())
                .filter(faixa -> salarioLiquido.compareTo(faixa.minimo) >= 0
                    && salarioLiquido.compareTo(faixa.maximo) <= 0)
                .findFirst()
                .orElse(Renda_premium);
    }
    //Metodo para calcular o limite de credito
    //pega o salario liquido e multiplica pelo multiplicador.
    public BigDecimal calcularLimite(BigDecimal salarioLiquido){
        return salarioLiquido.multiply(multiplicador).setScale(2, RoundingMode.HALF_UP);
    };
    //Metodo responsavel por verificar opções de parcelas,retorna uma lista de opcoes de parcelamento
    //Inicia com o minimo de parcelas (12) e acrescenta mais 6 de acordo com a renda salarial das nossas faixa de credito.
    public List<Integer> getOpcoesDeParcelamento(){
        List<Integer> opcoes = new ArrayList<>();
        for (int numeroDeParcelas = parcelasMinimas; numeroDeParcelas <= parcelasMinimas; numeroDeParcelas += 6){
            opcoes.add(numeroDeParcelas);
        }
        return opcoes;
    };
    //Metodo que calcula o valor da parcela aplicando os juros ao mes. soma e divide pelo numero de parcelas.
    public BigDecimal calcularParcelas(BigDecimal limite, int parcelas){
        BigDecimal meses = BigDecimal.valueOf(parcelas);
        BigDecimal jurosTotais = limite.multiply(taxaDeJuro.divide(BigDecimal.valueOf(100)))
                .multiply(meses);
        BigDecimal totalComJutos =limite.add(jurosTotais);
        return totalComJutos.divide(meses, 2, RoundingMode.HALF_UP);
    };
}
