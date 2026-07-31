package br.com.raizesdonordeste.repository;

import br.com.raizesdonordeste.domain.MetaUnidade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MetaUnidadeRepository extends JpaRepository<MetaUnidade, Long> {
    List<MetaUnidade> findByUnidadeId(Long unidadeId);
}
