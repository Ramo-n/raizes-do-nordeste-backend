package br.com.raizesdonordeste.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "cliente")
@Getter
@Setter
@NoArgsConstructor
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    private LocalDate dataNascimento;

    @Column(nullable = false)
    private boolean consentimentoLgpd = false;

    private LocalDateTime dataConsentimento;

    @Column(nullable = false)
    private boolean anonimizado = false;

    public void anonimizar() {
        this.nome = "ANONIMIZADO";
        this.email = "anonimizado-" + id + "@lgpd.invalid";
        this.dataNascimento = null;
        this.consentimentoLgpd = false;
        this.dataConsentimento = null;
        this.anonimizado = true;
    }
}
