package br.com.raizesdonordeste.web;

import br.com.raizesdonordeste.domain.Pedido;
import br.com.raizesdonordeste.service.PedidoService;
import br.com.raizesdonordeste.web.dto.Dtos.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
@Tag(name = "Pedidos", description = "Pedidos multicanal (app, totem, balcão, pick-up) — RF02, RF03, RF04, RF07")
public class PedidoController {

    private final PedidoService pedidoService;

    @PostMapping
    @Operation(summary = "Registrar pedido de qualquer canal (solicita o pagamento externo automaticamente)")
    public ResponseEntity<PedidoResp> criar(@Valid @RequestBody NovoPedido req) {
        Pedido pedido = pedidoService.criarPedido(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResp(pedido));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Acompanhar o pedido e seu status")
    public PedidoResp buscar(@PathVariable Long id) {
        return toResp(pedidoService.buscar(id));
    }

    @GetMapping
    @Operation(summary = "Listar pedidos de uma unidade")
    public List<PedidoResp> listarPorUnidade(@RequestParam Long unidadeId) {
        return pedidoService.listarPorUnidade(unidadeId).stream().map(PedidoController::toResp).toList();
    }

    @PostMapping("/{id}/avancar")
    @Operation(summary = "Avançar o status operacional (PAGO → EM_PREPARO → PRONTO → ENTREGUE)")
    public PedidoResp avancar(@PathVariable Long id) {
        return toResp(pedidoService.avancarStatus(id));
    }

    @PostMapping("/{id}/cancelamento")
    @Operation(summary = "Cancelar pedido (operação sensível — auditada)")
    public PedidoResp cancelar(@PathVariable Long id, @Valid @RequestBody CancelamentoReq req) {
        return toResp(pedidoService.cancelar(id, req.autor(), req.justificativa()));
    }

    @PostMapping("/{id}/desconto")
    @Operation(summary = "Aplicar desconto manual (operação sensível — auditada)")
    public PedidoResp desconto(@PathVariable Long id, @Valid @RequestBody DescontoReq req) {
        return toResp(pedidoService.aplicarDesconto(id, req.valorDesconto(), req.autor(), req.justificativa()));
    }

    static PedidoResp toResp(Pedido p) {
        return new PedidoResp(p.getId(), p.getUnidade().getId(),
                p.getCliente() != null ? p.getCliente().getId() : null,
                p.getCanal(), p.getStatus(), p.getDataHora(),
                p.getValorBruto(), p.getDesconto(), p.getValorTotal(),
                p.getItens().stream().map(i -> new ItemPedidoResp(i.getProduto().getId(),
                        i.getProduto().getNome(), i.getQuantidade(), i.getPrecoUnitario())).toList());
    }
}
