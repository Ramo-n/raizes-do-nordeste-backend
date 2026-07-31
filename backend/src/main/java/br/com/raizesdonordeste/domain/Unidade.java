package br.com.raizesdonordeste.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "unidade")
@Getter
@Setter
@NoArgsConstructor
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
}
