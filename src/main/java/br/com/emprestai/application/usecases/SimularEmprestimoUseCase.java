package br.com.emprestai.application.usecases;

import br.com.emprestai.application.controllers.dto.request.SimularEmprestimoRequest;
import br.com.emprestai.application.controllers.dto.response.PlanosResponse;
import br.com.emprestai.application.controllers.dto.response.SimularEmprestimoResponse;
import br.com.emprestai.domain.entity.cliente.Cliente;
import br.com.emprestai.domain.entity.simulacao.Simulacao;
import br.com.emprestai.domain.enums.emprestimo.FaixaCredito;
import br.com.emprestai.domain.regras.CalculoSalarioLiquidoRegra;
import br.com.emprestai.domain.regras.SimulaEmprestimoRegra;
import br.com.emprestai.infrastructure.entity.cliente.ClienteJpa;
import br.com.emprestai.infrastructure.entity.simulacao.SimulacaoJpa;
import br.com.emprestai.infrastructure.repositories.cliente.ClienteRepository;
import br.com.emprestai.infrastructure.repositories.simulacao.SimulacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class SimularEmprestimoUseCase {

    private static final BigDecimal SALARIO_MINIMO_BRUTO_PERMITIDO = new BigDecimal("1621.00");

    private final ClienteRepository clienteRepository;
    private final SimulacaoRepository simulacaoRepository;

    @Autowired
    public SimularEmprestimoUseCase(
            ClienteRepository clienteRepository,
            SimulacaoRepository simulacaoRepository
    ) {
        this.clienteRepository = clienteRepository;
        this.simulacaoRepository = simulacaoRepository;
    }

    public SimularEmprestimoResponse process(SimularEmprestimoRequest request) {

        String nome = request.nome();
        BigDecimal salarioBruto = request.salarioBruto();
        Optional<BigDecimal> valorDesejadoOpt = request.valorDesejado();

        if (salarioBruto.compareTo(SALARIO_MINIMO_BRUTO_PERMITIDO) < 0) {
            throw new IllegalArgumentException("O salário bruto informado é inferior ao mínimo permitido de R$ " + SALARIO_MINIMO_BRUTO_PERMITIDO);
        }

        Cliente cliente = new Cliente(nome, salarioBruto, BigDecimal.ZERO);
        CalculoSalarioLiquidoRegra regraLiquido = new CalculoSalarioLiquidoRegra();
        Cliente clienteLiquido = regraLiquido.run(cliente);
        BigDecimal salarioLiquido = clienteLiquido.getSalatioLiquido();

        FaixaCredito faixa = FaixaCredito.FaixaPara(salarioLiquido);
        BigDecimal limiteCredito = faixa.calcularLimite(salarioLiquido);

        BigDecimal valorParaSimulacao;

        if (valorDesejadoOpt.isPresent()) {
            BigDecimal valorDesejado = valorDesejadoOpt.get();

            if (valorDesejado.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("O valor desejado deve ser maior que zero.");
            }

            if (valorDesejado.compareTo(limiteCredito) > 0) {
                throw new IllegalArgumentException("O valor desejado de R$ " + valorDesejado + 
                        " excede o seu limite máximo aprovado de R$ " + limiteCredito);
            }

            valorParaSimulacao = valorDesejado;
        } else {
            valorParaSimulacao = limiteCredito;
        }

        // Variável efetivamente final para uso no lambda
        final BigDecimal valorFinalParaSimulacao = valorParaSimulacao;

        List<Integer> opcoes = faixa.getOpcoesDeParcelamento();
        List<Simulacao> simulacoes = opcoes.stream()
                .map(parcelas -> SimulaEmprestimoRegra.run(salarioLiquido, valorFinalParaSimulacao, parcelas))
                .toList();

        ClienteJpa clienteJpa = new ClienteJpa(nome, salarioBruto, salarioLiquido);
        ClienteJpa clienteSalvo = clienteRepository.save(clienteJpa);

        List<SimulacaoJpa> simulacaoJpas = simulacoes.stream()
                .map(sim -> new SimulacaoJpa(
                        clienteSalvo.getId(),
                        limiteCredito, // Mantém o limite máximo original
                        valorFinalParaSimulacao, // Novo campo com o valor simulado
                        sim.parcelas,
                        String.format("%d meses R$%.2f (%.1f%%)",
                                sim.parcelas,
                                sim.valorDasParcelas,
                                sim.porcentagemDoSalario)
                ))
                .toList();

        simulacaoRepository.saveAll(simulacaoJpas);

        List<PlanosResponse> planos = simulacoes.stream()
                .map(sim -> new PlanosResponse(
                        sim.parcelas,
                        sim.valorDasParcelas,
                        sim.porcentagemDoSalario,
                        faixa.taxaDeJuro.toString()
                ))
                .toList();

        return new SimularEmprestimoResponse(nome, salarioLiquido, faixa.name(), limiteCredito, valorFinalParaSimulacao, planos);
    }
}
