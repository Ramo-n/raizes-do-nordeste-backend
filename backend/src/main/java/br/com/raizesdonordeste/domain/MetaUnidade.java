package br.com.raizesdonordeste.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "meta_unidade",
        uniqueConstraints = @UniqueConstraint(columnNames = {"unidade_id", "ano_mes"}))
@Getter
@Setter
@NoArgsConstructor
public class MetaUnidade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "unidade_id")
    private Unidade unidade;

    @Column(name = "ano_mes", nullable = false, length = 7)
    private String anoMes;

    @Column(nullable = false)
    private BigDecimal metaVendas;
}
