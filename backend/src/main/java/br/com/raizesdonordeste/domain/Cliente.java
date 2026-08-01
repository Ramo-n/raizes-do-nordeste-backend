package br.com.raizesdonordeste.domain;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "cliente")
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

    public Long getId() {
        return this.id;
    }

    public String getNome() {
        return this.nome;
    }

    public String getEmail() {
        return this.email;
    }

    public LocalDate getDataNascimento() {
        return this.dataNascimento;
    }

    public boolean isConsentimentoLgpd() {
        return this.consentimentoLgpd;
    }

    public LocalDateTime getDataConsentimento() {
        return this.dataConsentimento;
    }

    public boolean isAnonimizado() {
        return this.anonimizado;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public void setConsentimentoLgpd(boolean consentimentoLgpd) {
        this.consentimentoLgpd = consentimentoLgpd;
    }

    public void setDataConsentimento(LocalDateTime dataConsentimento) {
        this.dataConsentimento = dataConsentimento;
    }

    public void setAnonimizado(boolean anonimizado) {
        this.anonimizado = anonimizado;
    }

    public Cliente() {
    }
}
