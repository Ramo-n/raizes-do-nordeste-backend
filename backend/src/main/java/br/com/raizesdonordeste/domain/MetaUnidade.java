package br.com.raizesdonordeste.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "meta_unidade", uniqueConstraints = @UniqueConstraint(columnNames = {"unidade_id", "ano_mes"}))
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

    public Long getId() {
        return this.id;
    }

    public Unidade getUnidade() {
        return this.unidade;
    }

    public String getAnoMes() {
        return this.anoMes;
    }

    public BigDecimal getMetaVendas() {
        return this.metaVendas;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUnidade(Unidade unidade) {
        this.unidade = unidade;
    }

    public void setAnoMes(String anoMes) {
        this.anoMes = anoMes;
    }

    public void setMetaVendas(BigDecimal metaVendas) {
        this.metaVendas = metaVendas;
    }

    public MetaUnidade() {
    }
}
