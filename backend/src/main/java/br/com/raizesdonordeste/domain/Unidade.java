package br.com.raizesdonordeste.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "unidade")
public class Unidade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nome;
    @Column(nullable = false)
    private String cidade;
    @Column(nullable = false)
    private String regiao;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoCozinha tipoCozinha;
    private String horarioFuncionamento;
    @Column(nullable = false)
    private boolean ativa = true;

    public Long getId() {
        return this.id;
    }

    public String getNome() {
        return this.nome;
    }

    public String getCidade() {
        return this.cidade;
    }

    public String getRegiao() {
        return this.regiao;
    }

    public TipoCozinha getTipoCozinha() {
        return this.tipoCozinha;
    }

    public String getHorarioFuncionamento() {
        return this.horarioFuncionamento;
    }

    public boolean isAtiva() {
        return this.ativa;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public void setRegiao(String regiao) {
        this.regiao = regiao;
    }

    public void setTipoCozinha(TipoCozinha tipoCozinha) {
        this.tipoCozinha = tipoCozinha;
    }

    public void setHorarioFuncionamento(String horarioFuncionamento) {
        this.horarioFuncionamento = horarioFuncionamento;
    }

    public void setAtiva(boolean ativa) {
        this.ativa = ativa;
    }

    public Unidade() {
    }
}
