package br.com.emprestai.application.controllers.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record SimularEmprestimoResponse(
        String nome,
        BigDecimal salarioLiquido,
        String faixa,
        BigDecimal limiteTotal,
        BigDecimal valorSimulado,
        List<PlanosResponse> planos
) { }
