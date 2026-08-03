package br.com.raizesdonordeste.service;

import br.com.raizesdonordeste.domain.MetaUnidade;
import br.com.raizesdonordeste.repository.MetaUnidadeRepository;
import br.com.raizesdonordeste.repository.PedidoRepository;
import br.com.raizesdonordeste.web.dto.Dtos.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class RelatorioService {
    private final PedidoRepository pedidoRepository;
    private final MetaUnidadeRepository metaUnidadeRepository;

    // RF08/RF10: vendas por unidade (matriz).
    @Transactional(readOnly = true)
    public List<VendasUnidade> vendasPorUnidade() {
        return pedidoRepository.vendasPorUnidade().stream().map(r -> new VendasUnidade((Long) r[0], (String) r[1], (String) r[2], (Long) r[3], (BigDecimal) r[4])).toList();
    }

    // RF08/RF10: vendas por região (matriz).
    @Transactional(readOnly = true)
    public List<VendasRegiao> vendasPorRegiao() {
        return pedidoRepository.vendasPorRegiao().stream().map(r -> new VendasRegiao((String) r[0], (Long) r[1], (BigDecimal) r[2])).toList();
    }

    // RF09: produtos mais consumidos.
    @Transactional(readOnly = true)
    public List<ProdutoConsumo> produtosMaisConsumidos() {
        return pedidoRepository.produtosMaisConsumidos().stream().map(r -> new ProdutoConsumo((Long) r[0], (String) r[1], (Long) r[2])).toList();
    }

    // RF20: metas e indicadores de desempenho por unidade.
    @Transactional(readOnly = true)
    public List<IndicadorUnidade> indicadoresDaUnidade(Long unidadeId) {
        BigDecimal vendas = vendasPorUnidade().stream().filter(v -> v.unidadeId().equals(unidadeId)).map(VendasUnidade::total).findFirst().orElse(BigDecimal.ZERO);
        List<MetaUnidade> metas = metaUnidadeRepository.findByUnidadeId(unidadeId);
        return metas.stream().map(m -> new IndicadorUnidade(unidadeId, m.getUnidade().getNome(), m.getAnoMes(), m.getMetaVendas(), vendas, m.getMetaVendas().signum() > 0 ? vendas.multiply(new BigDecimal("100")).divide(m.getMetaVendas(), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO)).toList();
    }

    public RelatorioService(PedidoRepository pedidoRepository, MetaUnidadeRepository metaUnidadeRepository) {
        this.pedidoRepository = pedidoRepository;
        this.metaUnidadeRepository = metaUnidadeRepository;
    }
}
