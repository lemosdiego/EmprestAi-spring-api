package br.com.emprestai.application.controllers.dto.response;

import java.math.BigDecimal;

public record PlanosResponse(
        Integer meses,
        BigDecimal valorParcela,
        BigDecimal percentualSalario,
        String jurosMensal
) { }
