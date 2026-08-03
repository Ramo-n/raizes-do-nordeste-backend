package br.com.raizesdonordeste;

import br.com.raizesdonordeste.domain.*;
import br.com.raizesdonordeste.repository.ContaFidelidadeRepository;
import br.com.raizesdonordeste.repository.EstoqueItemRepository;
import br.com.raizesdonordeste.service.*;
import br.com.raizesdonordeste.web.dto.Dtos.NovoItemPedido;
import br.com.raizesdonordeste.web.dto.Dtos.NovoPedido;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class FluxoPedidoTest {

    @Autowired PedidoService pedidoService;
    @Autowired EstoqueItemRepository estoqueItemRepository;
    @Autowired ContaFidelidadeRepository contaFidelidadeRepository;
    @Autowired AuditoriaService auditoriaService;

    private Pedido criarPedidoPadrao() {
        // Cliente 1 (com consentimento e 120 pontos) compra 2 tapiocas na unidade 1
        return pedidoService.criarPedido(new NovoPedido(1L, 1L, CanalPedido.APLICATIVO,
                List.of(new NovoItemPedido(1L, 2))));
    }

    @Test
    void criaPedidoComDescontoProgressivoESolicitaPagamento() {
        Pedido pedido = criarPedidoPadrao();
        assertThat(pedido.getStatus()).isEqualTo(StatusPedido.AGUARDANDO_PAGAMENTO);
        assertThat(pedido.getValorBruto()).isEqualByComparingTo("24.00");
        // 120 pontos → faixa de 5% de desconto progressivo (INF03)
        assertThat(pedido.getDesconto()).isEqualByComparingTo("1.20");
        assertThat(pedido.getValorTotal()).isEqualByComparingTo("22.80");
    }

    @Test
    void confirmacaoDePagamentoBaixaEstoqueEAcumulaPontos() {
        Pedido pedido = criarPedidoPadrao();
        int estoqueAntes = estoqueItemRepository.findByUnidadeIdAndProdutoId(1L, 1L).orElseThrow().getQuantidade();
        int pontosAntes = contaFidelidadeRepository.findByClienteId(1L).orElseThrow().getPontos();

        pedido = pedidoService.registrarResultadoPagamento(pedido.getId(), StatusPagamento.CONFIRMADO, "EXT-1");

        assertThat(pedido.getStatus()).isEqualTo(StatusPedido.PAGO);
        assertThat(estoqueItemRepository.findByUnidadeIdAndProdutoId(1L, 1L).orElseThrow().getQuantidade())
                .isEqualTo(estoqueAntes - 2);
        assertThat(contaFidelidadeRepository.findByClienteId(1L).orElseThrow().getPontos())
                .isEqualTo(pontosAntes + pedido.getValorTotal().intValue());
    }

    @Test
    void callbackDePagamentoEIdempotente() {
        Pedido pedido = criarPedidoPadrao();
        pedidoService.registrarResultadoPagamento(pedido.getId(), StatusPagamento.CONFIRMADO, "EXT-1");
        int estoqueAposPrimeiro = estoqueItemRepository.findByUnidadeIdAndProdutoId(1L, 1L).orElseThrow().getQuantidade();

        // o mesmo resultado enviado de novo não pode baixar o estoque duas vezes (RF18)
        pedidoService.registrarResultadoPagamento(pedido.getId(), StatusPagamento.CONFIRMADO, "EXT-1");

        assertThat(estoqueItemRepository.findByUnidadeIdAndProdutoId(1L, 1L).orElseThrow().getQuantidade())
                .isEqualTo(estoqueAposPrimeiro);
    }

    @Test
    void pagamentoRecusadoNaoBaixaEstoque() {
        Pedido pedido = criarPedidoPadrao();
        int estoqueAntes = estoqueItemRepository.findByUnidadeIdAndProdutoId(1L, 1L).orElseThrow().getQuantidade();

        pedido = pedidoService.registrarResultadoPagamento(pedido.getId(), StatusPagamento.RECUSADO, "EXT-1");

        assertThat(pedido.getStatus()).isEqualTo(StatusPedido.PAGAMENTO_RECUSADO);
        assertThat(estoqueItemRepository.findByUnidadeIdAndProdutoId(1L, 1L).orElseThrow().getQuantidade())
                .isEqualTo(estoqueAntes);
    }

    @Test
    void cancelamentoDevolveEstoqueEGeraAuditoria() {
        Pedido pedido = criarPedidoPadrao();
        pedidoService.registrarResultadoPagamento(pedido.getId(), StatusPagamento.CONFIRMADO, "EXT-1");
        int estoqueAposPagamento = estoqueItemRepository.findByUnidadeIdAndProdutoId(1L, 1L).orElseThrow().getQuantidade();

        Pedido cancelado = pedidoService.cancelar(pedido.getId(), "gerente-recife", "Cliente desistiu");

        assertThat(cancelado.getStatus()).isEqualTo(StatusPedido.CANCELADO);
        assertThat(estoqueItemRepository.findByUnidadeIdAndProdutoId(1L, 1L).orElseThrow().getQuantidade())
                .isEqualTo(estoqueAposPagamento + 2);
        assertThat(auditoriaService.listar(TipoOperacaoSensivel.CANCELAMENTO))
                .anyMatch(r -> cancelado.getId().equals(r.getPedidoId()));
    }

    @Test
    void descontoManualEAuditado() {
        Pedido pedido = criarPedidoPadrao();
        pedido = pedidoService.aplicarDesconto(pedido.getId(), new BigDecimal("2.00"),
                "atendente-01", "Cortesia por atraso");
        assertThat(pedido.getDesconto()).isEqualByComparingTo("3.20");
        assertThat(auditoriaService.listar(TipoOperacaoSensivel.DESCONTO)).isNotEmpty();
    }

    @Test
    void produtoForaDoCardapioDaUnidadeERejeitado() {
        // Bolo de macaxeira (3) não existe no cardápio da unidade 2
        assertThatThrownBy(() -> pedidoService.criarPedido(new NovoPedido(2L, null, CanalPedido.TOTEM,
                List.of(new NovoItemPedido(3L, 1)))))
                .isInstanceOf(NegocioException.class);
    }

    @Test
    void estoqueInsuficienteImpedePedido() {
        assertThatThrownBy(() -> pedidoService.criarPedido(new NovoPedido(1L, null, CanalPedido.BALCAO,
                List.of(new NovoItemPedido(1L, 9999)))))
                .isInstanceOf(NegocioException.class)
                .hasMessageContaining("Estoque insuficiente");
    }
}
