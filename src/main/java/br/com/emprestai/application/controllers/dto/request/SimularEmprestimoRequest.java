package br.com.emprestai.application.controllers.dto.request;

import java.math.BigDecimal;
import java.util.Optional;

public record SimularEmprestimoRequest(
        String nome,
        BigDecimal salarioBruto,
        Optional<BigDecimal> valorDesejado
) {
}
