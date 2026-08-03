package br.com.raizesdonordeste.service;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

// Simulação do serviço externo de pagamento (em produção seria o serviço real)
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
