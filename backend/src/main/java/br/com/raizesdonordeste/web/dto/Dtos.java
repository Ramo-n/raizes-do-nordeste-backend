package br.com.raizesdonordeste.web.dto;

import br.com.raizesdonordeste.domain.CanalPedido;
import br.com.raizesdonordeste.domain.StatusPagamento;
import br.com.raizesdonordeste.domain.StatusPedido;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public final class Dtos {
    private Dtos() {}

    public record ItemCardapio(Long produtoId, String nome, String descricao, String categoria,
                               BigDecimal preco, String variacaoRegional, boolean sazonal) {}

    public record NovoItemPedido(@NotNull Long produtoId, @Min(1) int quantidade) {}

    public record NovoPedido(@NotNull Long unidadeId,
                             Long clienteId,
                             @NotNull CanalPedido canal,
                             @NotEmpty List<@Valid NovoItemPedido> itens) {}

    public record ItemPedidoResp(Long produtoId, String nomeProduto, int quantidade, BigDecimal precoUnitario) {}

    public record PedidoResp(Long id, Long unidadeId, Long clienteId, CanalPedido canal,
                             StatusPedido status, LocalDateTime dataHora,
                             BigDecimal valorBruto, BigDecimal desconto, BigDecimal valorTotal,
                             List<ItemPedidoResp> itens) {}

    public record CancelamentoReq(@NotBlank String autor, @NotBlank String justificativa) {}

    public record DescontoReq(@NotBlank String autor, @NotBlank String justificativa,
                              @NotNull @DecimalMin("0.00") BigDecimal valorDesconto) {}

    public record ResultadoPagamentoReq(@NotBlank String referenciaExterna, @NotNull StatusPagamento status) {}

    public record NovoCliente(@NotBlank String nome, @NotBlank @Email String email,
                              LocalDate dataNascimento, boolean consentimentoLgpd) {}

    public record AjusteEstoqueReq(@NotBlank String autor, @NotBlank String justificativa, int novaQuantidade) {}

    public record VendasUnidade(Long unidadeId, String unidade, String regiao, long pedidos, BigDecimal total) {}

    public record VendasRegiao(String regiao, long pedidos, BigDecimal total) {}

    public record ProdutoConsumo(Long produtoId, String produto, long quantidade) {}

    public record IndicadorUnidade(Long unidadeId, String unidade, String anoMes,
                                   BigDecimal metaVendas, BigDecimal vendasRealizadas,
                                   BigDecimal percentualAtingido) {}

    public record FidelidadeResp(Long clienteId, int pontos, BigDecimal percentualDesconto, long frequenciaConsumo) {}
}
