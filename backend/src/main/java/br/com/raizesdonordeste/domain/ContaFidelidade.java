package br.com.raizesdonordeste.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "conta_fidelidade")
@Getter
@Setter
@NoArgsConstructor
public class ContaFidelidade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "cliente_id", unique = true)
    private Cliente cliente;

    @Column(nullable = false)
    private int pontos = 0;

    /** 1 ponto a cada R$1 gasto em pedidos confirmados. */
    public void acumular(BigDecimal valorPedido) {
        this.pontos += valorPedido.intValue();
    }

    /** Desconto progressivo por faixa de pontos acumulados (inferência INF03). */
    public BigDecimal percentualDescontoProgressivo() {
        if (pontos >= 1000) return new BigDecimal("0.15");
        if (pontos >= 500) return new BigDecimal("0.10");
        if (pontos >= 100) return new BigDecimal("0.05");
        return BigDecimal.ZERO;
    }
}
