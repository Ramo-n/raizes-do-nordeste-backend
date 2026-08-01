package br.com.raizesdonordeste.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "produto_unidade", uniqueConstraints = @UniqueConstraint(columnNames = {"produto_id", "unidade_id"}))
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

    public Long getId() {
        return this.id;
    }

    public Produto getProduto() {
        return this.produto;
    }

    public Unidade getUnidade() {
        return this.unidade;
    }

    public BigDecimal getPreco() {
        return this.preco;
    }

    public boolean isDisponivel() {
        return this.disponivel;
    }

    public boolean isSazonal() {
        return this.sazonal;
    }

    public Integer getMesInicioSazonalidade() {
        return this.mesInicioSazonalidade;
    }

    public Integer getMesFimSazonalidade() {
        return this.mesFimSazonalidade;
    }

    public String getVariacaoRegional() {
        return this.variacaoRegional;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public void setUnidade(Unidade unidade) {
        this.unidade = unidade;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }

    public void setSazonal(boolean sazonal) {
        this.sazonal = sazonal;
    }

    public void setMesInicioSazonalidade(Integer mesInicioSazonalidade) {
        this.mesInicioSazonalidade = mesInicioSazonalidade;
    }

    public void setMesFimSazonalidade(Integer mesFimSazonalidade) {
        this.mesFimSazonalidade = mesFimSazonalidade;
    }

    public void setVariacaoRegional(String variacaoRegional) {
        this.variacaoRegional = variacaoRegional;
    }

    public ProdutoUnidade() {
    }
}
