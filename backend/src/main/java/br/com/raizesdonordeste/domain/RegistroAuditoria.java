package br.com.raizesdonordeste.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "registro_auditoria")
@Getter
@Setter
@NoArgsConstructor
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
}
