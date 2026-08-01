package br.com.raizesdonordeste.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedido")
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false)
    @JoinColumn(name = "unidade_id")
    private Unidade unidade;
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CanalPedido canal;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusPedido status = StatusPedido.CRIADO;
    @Column(nullable = false)
    private LocalDateTime dataHora = LocalDateTime.now();
    @Column(nullable = false)
    private BigDecimal valorBruto = BigDecimal.ZERO;
    @Column(nullable = false)
    private BigDecimal desconto = BigDecimal.ZERO;
    @Column(nullable = false)
    private BigDecimal valorTotal = BigDecimal.ZERO;
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedido> itens = new ArrayList<>();

    public Long getId() {
        return this.id;
    }

    public Unidade getUnidade() {
        return this.unidade;
    }

    public Cliente getCliente() {
        return this.cliente;
    }

    public CanalPedido getCanal() {
        return this.canal;
    }

    public StatusPedido getStatus() {
        return this.status;
    }

    public LocalDateTime getDataHora() {
        return this.dataHora;
    }

    public BigDecimal getValorBruto() {
        return this.valorBruto;
    }

    public BigDecimal getDesconto() {
        return this.desconto;
    }

    public BigDecimal getValorTotal() {
        return this.valorTotal;
    }

    public List<ItemPedido> getItens() {
        return this.itens;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUnidade(Unidade unidade) {
        this.unidade = unidade;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public void setCanal(CanalPedido canal) {
        this.canal = canal;
    }

    public void setStatus(StatusPedido status) {
        this.status = status;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public void setValorBruto(BigDecimal valorBruto) {
        this.valorBruto = valorBruto;
    }

    public void setDesconto(BigDecimal desconto) {
        this.desconto = desconto;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public void setItens(List<ItemPedido> itens) {
        this.itens = itens;
    }

    public Pedido() {
    }
}
