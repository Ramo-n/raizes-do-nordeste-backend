package br.com.raizesdonordeste;

import br.com.raizesdonordeste.domain.Cliente;
import br.com.raizesdonordeste.domain.TipoOperacaoSensivel;
import br.com.raizesdonordeste.service.AuditoriaService;
import br.com.raizesdonordeste.service.CardapioService;
import br.com.raizesdonordeste.service.ClienteService;
import br.com.raizesdonordeste.web.dto.Dtos.ItemCardapio;
import br.com.raizesdonordeste.web.dto.Dtos.NovoCliente;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.MonthDay;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class LgpdECardapioTest {

    @Autowired ClienteService clienteService;
    @Autowired CardapioService cardapioService;
    @Autowired AuditoriaService auditoriaService;

    @Test
    void cardapioDaUnidadeSoTrazProdutosDoLocalERespeitaSazonalidade() {
        List<ItemCardapio> cardapioRecife = cardapioService.cardapioDaUnidade(1L);
        List<ItemCardapio> cardapioSp = cardapioService.cardapioDaUnidade(2L);

        // Unidade 2 (reduzida) não vende bolo de macaxeira nem combo
        assertThat(cardapioSp).extracting(ItemCardapio::nome)
                .doesNotContain("Bolo de macaxeira", "Café da manhã completo");

        // Canjica junina (sazonal maio–julho) só aparece no período
        boolean periodoJunino = LocalDate.now().getMonthValue() >= 5 && LocalDate.now().getMonthValue() <= 7;
        assertThat(cardapioRecife.stream().anyMatch(i -> i.nome().equals("Canjica junina")))
                .isEqualTo(periodoJunino);
    }

    @Test
    void consentimentoExplicitoAbreContaDeFidelidade() {
        Cliente c = clienteService.cadastrar(new NovoCliente("Ana", "ana@exemplo.com",
                LocalDate.of(2000, 1, 1), false));
        assertThat(c.isConsentimentoLgpd()).isFalse();

        c = clienteService.registrarConsentimento(c.getId());
        assertThat(c.isConsentimentoLgpd()).isTrue();
        assertThat(c.getDataConsentimento()).isNotNull();
        assertThat(clienteService.fidelidade(c.getId()).pontos()).isZero();
    }

    @Test
    void acessoADadosPessoaisEAuditado() {
        clienteService.consultar(1L, "matriz-analista");
        assertThat(auditoriaService.listar(TipoOperacaoSensivel.ACESSO_DADOS_PESSOAIS))
                .anyMatch(r -> Long.valueOf(1L).equals(r.getClienteId()));
    }

    @Test
    void anonimizacaoRemoveDadosPessoaisEEAuditada() {
        Cliente c = clienteService.anonimizar(1L, "dpo");
        assertThat(c.isAnonimizado()).isTrue();
        assertThat(c.getNome()).isEqualTo("ANONIMIZADO");
        assertThat(c.getEmail()).doesNotContain("maria");
        assertThat(c.getDataNascimento()).isNull();
        assertThat(auditoriaService.listar(TipoOperacaoSensivel.ANONIMIZACAO)).isNotEmpty();
    }
}
