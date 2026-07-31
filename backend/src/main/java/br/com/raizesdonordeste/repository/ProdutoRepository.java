package br.com.raizesdonordeste.repository;

import br.com.raizesdonordeste.domain.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}
