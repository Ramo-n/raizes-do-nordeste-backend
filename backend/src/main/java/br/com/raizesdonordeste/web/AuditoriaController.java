package br.com.raizesdonordeste.web;

import br.com.raizesdonordeste.domain.RegistroAuditoria;
import br.com.raizesdonordeste.domain.TipoOperacaoSensivel;
import br.com.raizesdonordeste.service.AuditoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auditoria")
@RequiredArgsConstructor
@Tag(name = "Auditoria", description = "Trilha de operações sensíveis e acessos a dados pessoais — RF07, RF16")
public class AuditoriaController {

    private final AuditoriaService auditoriaService;

    @GetMapping
    @Operation(summary = "Listar registros de auditoria (filtro opcional por tipo)")
    public List<RegistroAuditoria> listar(@RequestParam(required = false) TipoOperacaoSensivel tipo) {
        return auditoriaService.listar(tipo);
    }
}
