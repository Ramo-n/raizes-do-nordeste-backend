package br.com.raizesdonordeste.repository;

import br.com.raizesdonordeste.domain.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
