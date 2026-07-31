package br.com.raizesdonordeste.repository;

import br.com.raizesdonordeste.domain.ContaFidelidade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContaFidelidadeRepository extends JpaRepository<ContaFidelidade, Long> {
    Optional<ContaFidelidade> findByClienteId(Long clienteId);
}
