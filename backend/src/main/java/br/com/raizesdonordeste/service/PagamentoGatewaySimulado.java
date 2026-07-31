package br.com.raizesdonordeste.service;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Adaptador simulado do provedor externo de pagamento. Em produção seria
 * substituído por um adaptador real (o processamento é 100% externo — seção 5).
 */
@Component
public class PagamentoGatewaySimulado implements PagamentoGateway {

    @Override
    public String solicitarPagamento(Long pedidoId, BigDecimal valor) {
        return "EXT-" + UUID.randomUUID();
    }

    @Override
    public String nomeProvedor() {
        return "provedor-simulado";
    }
}
