package br.com.raizesdonordeste.repository;

import br.com.raizesdonordeste.domain.RegistroAuditoria;
import br.com.raizesdonordeste.domain.TipoOperacaoSensivel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegistroAuditoriaRepository extends JpaRepository<RegistroAuditoria, Long> {
    List<RegistroAuditoria> findByTipoOrderByDataHoraDesc(TipoOperacaoSensivel tipo);
    List<RegistroAuditoria> findAllByOrderByDataHoraDesc();
}
