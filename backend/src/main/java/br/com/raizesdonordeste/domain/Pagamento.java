package br.com.raizesdonordeste.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagamento")
public class Pagamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(optional = false)
    @JoinColumn(name = "pedido_id", unique = true)
    private Pedido pedido;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusPagamento status = StatusPagamento.SOLICITADO;
    @Column(nullable = false)
    private String provedorExterno;
    private String referenciaExterna;
    @Column(nullable = false)
    private LocalDateTime dataSolicitacao = LocalDateTime.now();
    private LocalDateTime dataResultado;

    public Long getId() {
        return this.id;
    }

    public Pedido getPedido() {
        return this.pedido;
    }

    public StatusPagamento getStatus() {
        return this.status;
    }

    public String getProvedorExterno() {
        return this.provedorExterno;
    }

    public String getReferenciaExterna() {
        return this.referenciaExterna;
    }

    public LocalDateTime getDataSolicitacao() {
        return this.dataSolicitacao;
    }

    public LocalDateTime getDataResultado() {
        return this.dataResultado;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public void setStatus(StatusPagamento status) {
        this.status = status;
    }

    public void setProvedorExterno(String provedorExterno) {
        this.provedorExterno = provedorExterno;
    }

    public void setReferenciaExterna(String referenciaExterna) {
        this.referenciaExterna = referenciaExterna;
    }

    public void setDataSolicitacao(LocalDateTime dataSolicitacao) {
        this.dataSolicitacao = dataSolicitacao;
    }

    public void setDataResultado(LocalDateTime dataResultado) {
        this.dataResultado = dataResultado;
    }

    public Pagamento() {
    }
}
