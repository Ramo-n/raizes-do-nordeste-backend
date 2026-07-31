package br.com.raizesdonordeste.web;

import br.com.raizesdonordeste.service.PedidoService;
import br.com.raizesdonordeste.web.dto.Dtos.PedidoResp;
import br.com.raizesdonordeste.web.dto.Dtos.ResultadoPagamentoReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pagamentos")
@RequiredArgsConstructor
@Tag(name = "Pagamentos", description = "Callback do serviço externo de pagamento — RF17, RF18")
public class PagamentoController {

    private final PedidoService pedidoService;

    @PostMapping("/{pedidoId}/resultado")
    @Operation(summary = "Callback (webhook) do provedor externo: registra CONFIRMADO/RECUSADO e atualiza o pedido (idempotente)")
    public PedidoResp resultado(@PathVariable Long pedidoId, @Valid @RequestBody ResultadoPagamentoReq req) {
        return PedidoController.toResp(
                pedidoService.registrarResultadoPagamento(pedidoId, req.status(), req.referenciaExterna()));
    }
}
