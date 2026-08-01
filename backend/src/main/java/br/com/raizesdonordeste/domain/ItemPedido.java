package br.com.raizesdonordeste.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "item_pedido")
public class ItemPedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false)
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;
    @ManyToOne(optional = false)
    @JoinColumn(name = "produto_id")
    private Produto produto;
    @Column(nullable = false)
    private int quantidade;
    @Column(nullable = false)
    private BigDecimal precoUnitario;

    public Long getId() {
        return this.id;
    }

    public Pedido getPedido() {
        return this.pedido;
    }

    public Produto getProduto() {
        return this.produto;
    }

    public int getQuantidade() {
        return this.quantidade;
    }

    public BigDecimal getPrecoUnitario() {
        return this.precoUnitario;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public void setPrecoUnitario(BigDecimal precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    public ItemPedido() {
    }
}
