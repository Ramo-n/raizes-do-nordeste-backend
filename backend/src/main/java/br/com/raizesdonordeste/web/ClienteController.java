package br.com.raizesdonordeste.web;

import br.com.raizesdonordeste.domain.Cliente;
import br.com.raizesdonordeste.service.ClienteService;
import br.com.raizesdonordeste.web.dto.Dtos.FidelidadeResp;
import br.com.raizesdonordeste.web.dto.Dtos.NovoCliente;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clientes")
@Tag(name = "Clientes e LGPD", description = "Cadastro, consentimento, anonimização e fidelidade — RF11 a RF16")
public class ClienteController {
    private final ClienteService clienteService;

    @PostMapping
    @Operation(summary = "Cadastrar cliente (consentimento LGPD explícito opcional no cadastro)")
    public ResponseEntity<Cliente> cadastrar(@Valid @RequestBody NovoCliente req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteService.cadastrar(req));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar dados pessoais (acesso auditado — LGPD)")
    public Cliente consultar(@PathVariable Long id, @RequestHeader("X-Autor") String autor) {
        return clienteService.consultar(id, autor);
    }

    @PostMapping("/{id}/consentimento")
    @Operation(summary = "Registrar consentimento LGPD explícito (abre a conta de fidelidade)")
    public Cliente consentimento(@PathVariable Long id) {
        return clienteService.registrarConsentimento(id);
    }

    @PostMapping("/{id}/anonimizacao")
    @Operation(summary = "Anonimizar dados pessoais (irreversível, auditado — LGPD)")
    public Cliente anonimizar(@PathVariable Long id, @RequestHeader("X-Autor") String autor) {
        return clienteService.anonimizar(id, autor);
    }

    @GetMapping("/{id}/fidelidade")
    @Operation(summary = "Pontos, desconto progressivo e frequência de consumo do cliente")
    public FidelidadeResp fidelidade(@PathVariable Long id) {
        return clienteService.fidelidade(id);
    }

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }
}
