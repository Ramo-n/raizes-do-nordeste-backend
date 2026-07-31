package br.com.raizesdonordeste.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "produto_unidade",
        uniqueConstraints = @UniqueConstraint(columnNames = {"produto_id", "unidade_id"}))
@Getter
@Setter
@NoArgsConstructor
public class ProdutoUnidade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "produto_id")
    private Produto produto;

    @ManyToOne(optional = false)
    @JoinColumn(name = "unidade_id")
    private Unidade unidade;

    @Column(nullable = false)
    private BigDecimal preco;

    @Column(nullable = false)
    private boolean disponivel = true;

    @Column(nullable = false)
    private boolean sazonal = false;

    private Integer mesInicioSazonalidade;
    private Integer mesFimSazonalidade;

    private String variacaoRegional;

    public boolean disponivelEm(LocalDate data) {
        if (!disponivel) return false;
        if (!sazonal) return true;
        if (mesInicioSazonalidade == null || mesFimSazonalidade == null) return true;
        int mes = data.getMonthValue();
        if (mesInicioSazonalidade <= mesFimSazonalidade) {
            return mes >= mesInicioSazonalidade && mes <= mesFimSazonalidade;
        }
        return mes >= mesInicioSazonalidade || mes <= mesFimSazonalidade;
    }
}
