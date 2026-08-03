package br.com.raizesdonordeste.service;

import br.com.raizesdonordeste.repository.ProdutoUnidadeRepository;
import br.com.raizesdonordeste.repository.UnidadeRepository;
import br.com.raizesdonordeste.web.dto.Dtos.ItemCardapio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
public class CardapioService {
    private final UnidadeRepository unidadeRepository;
    private final ProdutoUnidadeRepository produtoUnidadeRepository;

    // RF01/RF05: cardápio da unidade contendo só produtos disponíveis no local e na data.
    @Transactional(readOnly = true)
    public List<ItemCardapio> cardapioDaUnidade(Long unidadeId) {
        if (!unidadeRepository.existsById(unidadeId)) {
            throw new RecursoNaoEncontradoException("Unidade não encontrada: " + unidadeId);
        }
        LocalDate hoje = LocalDate.now();
        return produtoUnidadeRepository.findByUnidadeId(unidadeId).stream().filter(pu -> pu.disponivelEm(hoje)).map(pu -> new ItemCardapio(pu.getProduto().getId(), pu.getProduto().getNome(), pu.getProduto().getDescricao(), pu.getProduto().getCategoria(), pu.getPreco(), pu.getVariacaoRegional(), pu.isSazonal())).toList();
    }

    public CardapioService(UnidadeRepository unidadeRepository, ProdutoUnidadeRepository produtoUnidadeRepository) {
        this.unidadeRepository = unidadeRepository;
        this.produtoUnidadeRepository = produtoUnidadeRepository;
    }
}
