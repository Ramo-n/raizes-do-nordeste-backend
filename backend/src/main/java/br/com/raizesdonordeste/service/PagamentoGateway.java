package br.com.raizesdonordeste.service;

import java.math.BigDecimal;

// Interface para falar com o serviço externo de pagamento (o resultado chega depois pelo endpoint de retorno)
public interface PagamentoGateway {

    // pede o pagamento e retorna a referência gerada pelo serviço externo
    String solicitarPagamento(Long pedidoId, BigDecimal valor);

    String nomeProvedor();
}
