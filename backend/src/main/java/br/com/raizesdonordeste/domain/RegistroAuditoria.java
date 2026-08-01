package br.com.raizesdonordeste.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "registro_auditoria")
public class RegistroAuditoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoOperacaoSensivel tipo;
    @Column(nullable = false)
    private String autor;
    @Column(nullable = false)
    private LocalDateTime dataHora = LocalDateTime.now();
    @Column(nullable = false, length = 1000)
    private String detalhes;
    @Column(length = 1000)
    private String justificativa;
    private Long pedidoId;
    private Long clienteId;

    public Long getId() {
        return this.id;
    }

    public TipoOperacaoSensivel getTipo() {
        return this.tipo;
    }

    public String getAutor() {
        return this.autor;
    }

    public LocalDateTime getDataHora() {
        return this.dataHora;
    }

    public String getDetalhes() {
        return this.detalhes;
    }

    public String getJustificativa() {
        return this.justificativa;
    }

    public Long getPedidoId() {
        return this.pedidoId;
    }

    public Long getClienteId() {
        return this.clienteId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTipo(TipoOperacaoSensivel tipo) {
        this.tipo = tipo;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public void setDetalhes(String detalhes) {
        this.detalhes = detalhes;
    }

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }

    public void setPedidoId(Long pedidoId) {
        this.pedidoId = pedidoId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public RegistroAuditoria() {
    }
}
