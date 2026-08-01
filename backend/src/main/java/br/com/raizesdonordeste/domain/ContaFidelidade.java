package br.com.raizesdonordeste.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "conta_fidelidade")
public class ContaFidelidade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(optional = false)
    @JoinColumn(name = "cliente_id", unique = true)
    private Cliente cliente;
    @Column(nullable = false)
    private int pontos = 0;

    /**
     * 1 ponto a cada R$1 gasto em pedidos confirmados.
     */
    public void acumular(BigDecimal valorPedido) {
        this.pontos += valorPedido.intValue();
    }

    /**
     * Desconto progressivo por faixa de pontos acumulados (inferência INF03).
     */
    public BigDecimal percentualDescontoProgressivo() {
        if (pontos >= 1000) return new BigDecimal("0.15");
        if (pontos >= 500) return new BigDecimal("0.10");
        if (pontos >= 100) return new BigDecimal("0.05");
        return BigDecimal.ZERO;
    }

    public Long getId() {
        return this.id;
    }

    public Cliente getCliente() {
        return this.cliente;
    }

    public int getPontos() {
        return this.pontos;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public void setPontos(int pontos) {
        this.pontos = pontos;
    }

    public ContaFidelidade() {
    }
}
