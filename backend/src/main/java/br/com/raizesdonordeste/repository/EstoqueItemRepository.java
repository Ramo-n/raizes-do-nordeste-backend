package br.com.raizesdonordeste.repository;

import br.com.raizesdonordeste.domain.EstoqueItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EstoqueItemRepository extends JpaRepository<EstoqueItem, Long> {
    List<EstoqueItem> findByUnidadeId(Long unidadeId);
    Optional<EstoqueItem> findByUnidadeIdAndProdutoId(Long unidadeId, Long produtoId);
}
