package br.com.raizesdonordeste.web;

import br.com.raizesdonordeste.domain.EstoqueItem;
import br.com.raizesdonordeste.service.EstoqueService;
import br.com.raizesdonordeste.web.dto.Dtos.AjusteEstoqueReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estoque")
@RequiredArgsConstructor
@Tag(name = "Estoque", description = "Controle de estoque local por unidade — RF06, RF07")
public class EstoqueController {

    private final EstoqueService estoqueService;

    @GetMapping
    @Operation(summary = "Consultar estoque de uma unidade")
    public List<EstoqueItem> porUnidade(@RequestParam Long unidadeId) {
        return estoqueService.estoqueDaUnidade(unidadeId);
    }

    @PostMapping("/{itemId}/ajuste")
    @Operation(summary = "Ajuste manual de estoque (operação sensível — auditada)")
    public EstoqueItem ajustar(@PathVariable Long itemId, @Valid @RequestBody AjusteEstoqueReq req) {
        return estoqueService.ajustar(itemId, req.novaQuantidade(), req.autor(), req.justificativa());
    }
}
