package br.com.emprestai.application.controllers.dto.request;

import java.math.BigDecimal;

public record SimularEmprestimoRequest(String nome, BigDecimal salarioBruto) {
}
