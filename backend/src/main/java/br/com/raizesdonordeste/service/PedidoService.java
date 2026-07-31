package br.com.raizesdonordeste.service;

import br.com.raizesdonordeste.domain.*;
import br.com.raizesdonordeste.repository.*;
import br.com.raizesdonordeste.web.dto.Dtos.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final UnidadeRepository unidadeRepository;
    private final ClienteRepository clienteRepository;
    private final ProdutoUnidadeRepository produtoUnidadeRepository;
    private final EstoqueItemRepository estoqueItemRepository;
    private final PagamentoRepository pagamentoRepository;
    private final ContaFidelidadeRepository contaFidelidadeRepository;
    private final PagamentoGateway pagamentoGateway;
    private final AuditoriaService auditoriaService;

    /** RF02/RF03: registra pedido de qualquer canal; RF17: solicita pagamento externo. */
    @Transactional
    public Pedido criarPedido(NovoPedido req) {
        Unidade unidade = unidadeRepository.findById(req.unidadeId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Unidade não encontrada: " + req.unidadeId()));
        if (!unidade.isAtiva()) {
            throw new NegocioException("Unidade inativa não recebe pedidos");
        }

        Pedido pedido = new Pedido();
        pedido.setUnidade(unidade);
        pedido.setCanal(req.canal());

        if (req.clienteId() != null) {
            Cliente cliente = clienteRepository.findById(req.clienteId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado: " + req.clienteId()));
            pedido.setCliente(cliente);
        }

        LocalDate hoje = LocalDate.now();
        BigDecimal bruto = BigDecimal.ZERO;
        for (NovoItemPedido item : req.itens()) {
            ProdutoUnidade pu = produtoUnidadeRepository
                    .findByUnidadeIdAndProdutoId(unidade.getId(), item.produtoId())
                    .orElseThrow(() -> new NegocioException(
                            "Produto " + item.produtoId() + " não pertence ao cardápio da unidade " + unidade.getId()));
            if (!pu.disponivelEm(hoje)) {
                throw new NegocioException("Produto indisponível nesta unidade/período: " + pu.getProduto().getNome());
            }
            EstoqueItem estoque = estoqueItemRepository
                    .findByUnidadeIdAndProdutoId(unidade.getId(), item.produtoId())
                    .orElseThrow(() -> new NegocioException(
                            "Sem estoque cadastrado para o produto " + pu.getProduto().getNome()));
            if (estoque.getQuantidade() < item.quantidade()) {
                throw new NegocioException("Estoque insuficiente para " + pu.getProduto().getNome());
            }

            ItemPedido ip = new ItemPedido();
            ip.setPedido(pedido);
            ip.setProduto(pu.getProduto());
            ip.setQuantidade(item.quantidade());
            ip.setPrecoUnitario(pu.getPreco());
            pedido.getItens().add(ip);
            bruto = bruto.add(pu.getPreco().multiply(BigDecimal.valueOf(item.quantidade())));
        }

        pedido.setValorBruto(bruto);
        pedido.setDesconto(descontoFidelidade(pedido, bruto));
        pedido.setValorTotal(bruto.subtract(pedido.getDesconto()));
        pedido.setStatus(StatusPedido.AGUARDANDO_PAGAMENTO);
        pedido = pedidoRepository.save(pedido);

        Pagamento pagamento = new Pagamento();
        pagamento.setPedido(pedido);
        pagamento.setProvedorExterno(pagamentoGateway.nomeProvedor());
        pagamento.setReferenciaExterna(pagamentoGateway.solicitarPagamento(pedido.getId(), pedido.getValorTotal()));
        pagamentoRepository.save(pagamento);

        return pedido;
    }

    /** RF12: desconto progressivo do programa de fidelidade (INF03). */
    private BigDecimal descontoFidelidade(Pedido pedido, BigDecimal bruto) {
        if (pedido.getCliente() == null || !pedido.getCliente().isConsentimentoLgpd()) {
            return BigDecimal.ZERO;
        }
        return contaFidelidadeRepository.findByClienteId(pedido.getCliente().getId())
                .map(conta -> bruto.multiply(conta.percentualDescontoProgressivo())
                        .setScale(2, RoundingMode.HALF_UP))
                .orElse(BigDecimal.ZERO);
    }

    /** RF17/RF18: registra o resultado do pagamento externo e atualiza o pedido (idempotente). */
    @Transactional
    public Pedido registrarResultadoPagamento(Long pedidoId, StatusPagamento resultado, String referenciaExterna) {
        Pedido pedido = buscar(pedidoId);
        Pagamento pagamento = pagamentoRepository.findByPedidoId(pedidoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pagamento não encontrado para o pedido " + pedidoId));

        if (pagamento.getStatus() != StatusPagamento.SOLICITADO) {
            return pedido; // idempotência: resultado já processado
        }
        if (resultado != StatusPagamento.CONFIRMADO && resultado != StatusPagamento.RECUSADO) {
            throw new NegocioException("Resultado de pagamento inválido: " + resultado);
        }

        pagamento.setStatus(resultado);
        pagamento.setReferenciaExterna(referenciaExterna);
        pagamento.setDataResultado(java.time.LocalDateTime.now());

        if (resultado == StatusPagamento.CONFIRMADO) {
            pedido.setStatus(StatusPedido.PAGO);
            baixarEstoque(pedido);
            acumularPontos(pedido);
        } else {
            pedido.setStatus(StatusPedido.PAGAMENTO_RECUSADO);
        }
        return pedido;
    }

    /** RF06/INF04: baixa de estoque da unidade quando o pedido é confirmado. */
    private void baixarEstoque(Pedido pedido) {
        for (ItemPedido item : pedido.getItens()) {
            EstoqueItem estoque = estoqueItemRepository
                    .findByUnidadeIdAndProdutoId(pedido.getUnidade().getId(), item.getProduto().getId())
                    .orElseThrow(() -> new NegocioException("Estoque não encontrado para " + item.getProduto().getNome()));
            if (estoque.getQuantidade() < item.getQuantidade()) {
                throw new NegocioException("Estoque insuficiente para " + item.getProduto().getNome());
            }
            estoque.setQuantidade(estoque.getQuantidade() - item.getQuantidade());
        }
    }

    /** RF11: acumula pontos apenas com consentimento LGPD (RF14). */
    private void acumularPontos(Pedido pedido) {
        if (pedido.getCliente() == null || !pedido.getCliente().isConsentimentoLgpd()) return;
        contaFidelidadeRepository.findByClienteId(pedido.getCliente().getId())
                .ifPresent(conta -> conta.acumular(pedido.getValorTotal()));
    }

    /** RF04: avanço do status operacional do pedido. */
    @Transactional
    public Pedido avancarStatus(Long pedidoId) {
        Pedido pedido = buscar(pedidoId);
        StatusPedido proximo = switch (pedido.getStatus()) {
            case PAGO -> StatusPedido.EM_PREPARO;
            case EM_PREPARO -> StatusPedido.PRONTO;
            case PRONTO -> StatusPedido.ENTREGUE;
            default -> throw new NegocioException(
                    "Não é possível avançar o pedido no status " + pedido.getStatus());
        };
        pedido.setStatus(proximo);
        return pedido;
    }

    /** RF07: cancelamento é operação sensível — sempre auditada. */
    @Transactional
    public Pedido cancelar(Long pedidoId, String autor, String justificativa) {
        Pedido pedido = buscar(pedidoId);
        if (pedido.getStatus() == StatusPedido.ENTREGUE || pedido.getStatus() == StatusPedido.CANCELADO) {
            throw new NegocioException("Pedido não pode ser cancelado no status " + pedido.getStatus());
        }
        boolean estavaConfirmado = pedido.getStatus() == StatusPedido.PAGO
                || pedido.getStatus() == StatusPedido.EM_PREPARO
                || pedido.getStatus() == StatusPedido.PRONTO;
        if (estavaConfirmado) {
            devolverEstoque(pedido);
        }
        pedido.setStatus(StatusPedido.CANCELADO);
        auditoriaService.registrar(TipoOperacaoSensivel.CANCELAMENTO, autor,
                "Cancelamento do pedido " + pedidoId, justificativa, pedidoId,
                pedido.getCliente() != null ? pedido.getCliente().getId() : null);
        return pedido;
    }

    private void devolverEstoque(Pedido pedido) {
        for (ItemPedido item : pedido.getItens()) {
            estoqueItemRepository
                    .findByUnidadeIdAndProdutoId(pedido.getUnidade().getId(), item.getProduto().getId())
                    .ifPresent(e -> e.setQuantidade(e.getQuantidade() + item.getQuantidade()));
        }
    }

    /** RF07: desconto manual é operação sensível — sempre auditada. */
    @Transactional
    public Pedido aplicarDesconto(Long pedidoId, BigDecimal valorDesconto, String autor, String justificativa) {
        Pedido pedido = buscar(pedidoId);
        if (pedido.getStatus() != StatusPedido.CRIADO && pedido.getStatus() != StatusPedido.AGUARDANDO_PAGAMENTO) {
            throw new NegocioException("Desconto só pode ser aplicado antes do pagamento");
        }
        if (valorDesconto.compareTo(pedido.getValorBruto()) > 0) {
            throw new NegocioException("Desconto não pode exceder o valor bruto do pedido");
        }
        pedido.setDesconto(pedido.getDesconto().add(valorDesconto));
        pedido.setValorTotal(pedido.getValorBruto().subtract(pedido.getDesconto()));
        auditoriaService.registrar(TipoOperacaoSensivel.DESCONTO, autor,
                "Desconto de R$ " + valorDesconto + " no pedido " + pedidoId, justificativa, pedidoId,
                pedido.getCliente() != null ? pedido.getCliente().getId() : null);
        return pedido;
    }

    @Transactional(readOnly = true)
    public Pedido buscar(Long pedidoId) {
        return pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pedido não encontrado: " + pedidoId));
    }

    @Transactional(readOnly = true)
    public List<Pedido> listarPorUnidade(Long unidadeId) {
        return pedidoRepository.findByUnidadeId(unidadeId);
    }
}
