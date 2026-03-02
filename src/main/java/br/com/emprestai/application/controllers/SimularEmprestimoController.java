package br.com.emprestai.application.controllers;


import br.com.emprestai.application.controllers.dto.request.SimularEmprestimoRequest;
import br.com.emprestai.application.controllers.dto.response.SimularEmprestimoResponse;
import br.com.emprestai.application.usecases.SimularEmprestimoUseCase;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/simulacoes")
public class SimularEmprestimoController {
    private final SimularEmprestimoUseCase useCase;

    public SimularEmprestimoController(SimularEmprestimoUseCase useCase){
        this.useCase = useCase;
    }
    @PostMapping
    public SimularEmprestimoResponse simulate(@RequestBody SimularEmprestimoRequest request){
        return useCase.process(request);
    }
}
