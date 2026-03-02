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
import java.util.stream.Collectors;

@Service
public class SimularEmprestimoUseCase {

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

        Cliente cliente = new Cliente(nome, salarioBruto, BigDecimal.ZERO);
        CalculoSalarioLiquidoRegra regraLiquido = new CalculoSalarioLiquidoRegra();
        Cliente clienteLiquido = regraLiquido.run(cliente);
        BigDecimal salarioLiquido = clienteLiquido.getSalatioLiquido();

        FaixaCredito faixa = FaixaCredito.FaixaPara(salarioLiquido);
        BigDecimal limite = faixa.calcularLimite(salarioLiquido);

        SimulaEmprestimoRegra regraSimulacao = new SimulaEmprestimoRegra();
        List<Integer> opcoes = faixa.getOpcoesDeParcelamento();
        List<Simulacao> simulacoes = opcoes.stream()
                .map(parcelas -> regraSimulacao.run(salarioLiquido, parcelas))
                .toList();

        ClienteJpa clienteJpa = new ClienteJpa(nome, salarioBruto, salarioLiquido);
        ClienteJpa clienteSalvo = clienteRepository.save(clienteJpa);


        List<SimulacaoJpa> simulacaoJpas = simulacoes.stream()
                .map(sim -> new SimulacaoJpa(
                        clienteSalvo.getId(),
                        limite,
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

        return new SimularEmprestimoResponse(nome, salarioLiquido, faixa.name(), limite, planos);
    }
}
