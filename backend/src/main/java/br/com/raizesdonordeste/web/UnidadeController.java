package br.com.raizesdonordeste.web;

import br.com.raizesdonordeste.domain.Unidade;
import br.com.raizesdonordeste.repository.UnidadeRepository;
import br.com.raizesdonordeste.service.CardapioService;
import br.com.raizesdonordeste.service.RecursoNaoEncontradoException;
import br.com.raizesdonordeste.web.dto.Dtos.ItemCardapio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/unidades")
@Tag(name = "Unidades", description = "Unidades da franquia e seus cardápios — RF01, RF05, RF19")
public class UnidadeController {
    private final UnidadeRepository unidadeRepository;
    private final CardapioService cardapioService;

    @GetMapping
    @Operation(summary = "Listar unidades da rede")
    public List<Unidade> listar() {
        return unidadeRepository.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Detalhar uma unidade")
    public Unidade buscar(@PathVariable Long id) {
        return unidadeRepository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Unidade não encontrada: " + id));
    }

    @PostMapping
    @Operation(summary = "Cadastrar unidade (matriz)")
    public ResponseEntity<Unidade> criar(@Valid @RequestBody Unidade unidade) {
        unidade.setId(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(unidadeRepository.save(unidade));
    }

    @GetMapping("/{id}/cardapio")
    @Operation(summary = "Cardápio da unidade: só produtos disponíveis no local e no período atual")
    public List<ItemCardapio> cardapio(@PathVariable Long id) {
        return cardapioService.cardapioDaUnidade(id);
    }

    public UnidadeController(UnidadeRepository unidadeRepository, CardapioService cardapioService) {
        this.unidadeRepository = unidadeRepository;
        this.cardapioService = cardapioService;
    }
}
