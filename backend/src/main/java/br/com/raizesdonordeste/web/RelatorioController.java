package br.com.raizesdonordeste.web;

import br.com.raizesdonordeste.service.RelatorioService;
import br.com.raizesdonordeste.web.dto.Dtos.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/relatorios")
@RequiredArgsConstructor
@Tag(name = "Relatórios (Matriz)", description = "Indicadores consolidados da franqueadora — RF08, RF09, RF10, RF20")
public class RelatorioController {

    private final RelatorioService relatorioService;

    @GetMapping("/vendas-por-unidade")
    @Operation(summary = "Vendas consolidadas por unidade")
    public List<VendasUnidade> vendasPorUnidade() {
        return relatorioService.vendasPorUnidade();
    }

    @GetMapping("/vendas-por-regiao")
    @Operation(summary = "Vendas consolidadas por região")
    public List<VendasRegiao> vendasPorRegiao() {
        return relatorioService.vendasPorRegiao();
    }

    @GetMapping("/produtos-mais-consumidos")
    @Operation(summary = "Ranking de produtos mais consumidos")
    public List<ProdutoConsumo> produtosMaisConsumidos() {
        return relatorioService.produtosMaisConsumidos();
    }

    @GetMapping("/indicadores/{unidadeId}")
    @Operation(summary = "Metas e indicadores de desempenho da unidade")
    public List<IndicadorUnidade> indicadores(@PathVariable Long unidadeId) {
        return relatorioService.indicadoresDaUnidade(unidadeId);
    }
}
