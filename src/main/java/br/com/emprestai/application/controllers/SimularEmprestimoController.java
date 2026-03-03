package br.com.emprestai.application.controllers;


import br.com.emprestai.application.controllers.dto.request.SimularEmprestimoRequest;
import br.com.emprestai.application.controllers.dto.response.SimularEmprestimoResponse;
import br.com.emprestai.application.usecases.SimularEmprestimoUseCase;
import org.springframework.web.bind.annotation.*;

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
