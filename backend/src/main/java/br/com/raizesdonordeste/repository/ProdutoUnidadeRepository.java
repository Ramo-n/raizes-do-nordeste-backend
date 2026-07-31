package br.com.raizesdonordeste.repository;

import br.com.raizesdonordeste.domain.ProdutoUnidade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProdutoUnidadeRepository extends JpaRepository<ProdutoUnidade, Long> {
    List<ProdutoUnidade> findByUnidadeId(Long unidadeId);
    Optional<ProdutoUnidade> findByUnidadeIdAndProdutoId(Long unidadeId, Long produtoId);
}
