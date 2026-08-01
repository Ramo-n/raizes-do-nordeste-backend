package br.com.raizesdonordeste.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "estoque_item", uniqueConstraints = @UniqueConstraint(columnNames = {"unidade_id", "produto_id"}))
public class EstoqueItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false)
    @JoinColumn(name = "unidade_id")
    private Unidade unidade;
    @ManyToOne(optional = false)
    @JoinColumn(name = "produto_id")
    private Produto produto;
    @Column(nullable = false)
    private int quantidade;
    @Column(nullable = false)
    private int quantidadeMinima;

    public Long getId() {
        return this.id;
    }

    public Unidade getUnidade() {
        return this.unidade;
    }

    public Produto getProduto() {
        return this.produto;
    }

    public int getQuantidade() {
        return this.quantidade;
    }

    public int getQuantidadeMinima() {
        return this.quantidadeMinima;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUnidade(Unidade unidade) {
        this.unidade = unidade;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public void setQuantidadeMinima(int quantidadeMinima) {
        this.quantidadeMinima = quantidadeMinima;
    }

    public EstoqueItem() {
    }
}
