package br.com.raizesdonordeste.service;

import java.math.BigDecimal;

/**
 * Porta de integração com o serviço externo de pagamento (estudo de caso, seção 5).
 * O sistema apenas solicita o pagamento; o resultado chega de forma assíncrona
 * pelo callback (POST /api/pagamentos/{pedidoId}/resultado).
 */
public interface PagamentoGateway {

    /** Solicita o pagamento ao provedor externo e retorna a referência externa gerada. */
    String solicitarPagamento(Long pedidoId, BigDecimal valor);

    String nomeProvedor();
}
