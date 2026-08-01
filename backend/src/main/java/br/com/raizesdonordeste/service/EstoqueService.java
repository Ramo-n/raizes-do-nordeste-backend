package br.com.raizesdonordeste.service;

import br.com.raizesdonordeste.domain.EstoqueItem;
import br.com.raizesdonordeste.domain.TipoOperacaoSensivel;
import br.com.raizesdonordeste.repository.EstoqueItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class EstoqueService {
    private final EstoqueItemRepository estoqueItemRepository;
    private final AuditoriaService auditoriaService;

    /**
     * RF06: consulta do estoque local da unidade.
     */
    @Transactional(readOnly = true)
    public List<EstoqueItem> estoqueDaUnidade(Long unidadeId) {
        return estoqueItemRepository.findByUnidadeId(unidadeId);
    }

    /**
     * RF07: ajuste manual de estoque é operação sensível — sempre auditada.
     */
    @Transactional
    public EstoqueItem ajustar(Long estoqueItemId, int novaQuantidade, String autor, String justificativa) {
        EstoqueItem item = estoqueItemRepository.findById(estoqueItemId).orElseThrow(() -> new RecursoNaoEncontradoException("Item de estoque não encontrado: " + estoqueItemId));
        if (novaQuantidade < 0) {
            throw new NegocioException("Quantidade de estoque não pode ser negativa");
        }
        int anterior = item.getQuantidade();
        item.setQuantidade(novaQuantidade);
        auditoriaService.registrar(TipoOperacaoSensivel.AJUSTE_ESTOQUE, autor, "Ajuste de estoque do item " + estoqueItemId + " (" + item.getProduto().getNome() + ") de " + anterior + " para " + novaQuantidade + " na unidade " + item.getUnidade().getId(), justificativa, null, null);
        return item;
    }

    public EstoqueService(EstoqueItemRepository estoqueItemRepository, final AuditoriaService auditoriaService) {
        this.estoqueItemRepository = estoqueItemRepository;
        this.auditoriaService = auditoriaService;
    }
}
