package br.com.raizesdonordeste.service;

import br.com.raizesdonordeste.domain.Cliente;
import br.com.raizesdonordeste.domain.ContaFidelidade;
import br.com.raizesdonordeste.domain.TipoOperacaoSensivel;
import br.com.raizesdonordeste.repository.ClienteRepository;
import br.com.raizesdonordeste.repository.ContaFidelidadeRepository;
import br.com.raizesdonordeste.repository.PedidoRepository;
import br.com.raizesdonordeste.web.dto.Dtos.FidelidadeResp;
import br.com.raizesdonordeste.web.dto.Dtos.NovoCliente;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class ClienteService {
    private final ClienteRepository clienteRepository;
    private final ContaFidelidadeRepository contaFidelidadeRepository;
    private final PedidoRepository pedidoRepository;
    private final AuditoriaService auditoriaService;

    // RF14: cadastro com registro de consentimento explícito; RF11: adesão à fidelidade.
    @Transactional
    public Cliente cadastrar(NovoCliente req) {
        Cliente cliente = new Cliente();
        cliente.setNome(req.nome());
        cliente.setEmail(req.email());
        cliente.setDataNascimento(req.dataNascimento());
        if (req.consentimentoLgpd()) {
            cliente.setConsentimentoLgpd(true);
            cliente.setDataConsentimento(LocalDateTime.now());
        }
        cliente = clienteRepository.save(cliente);
        if (cliente.isConsentimentoLgpd()) {
            ContaFidelidade conta = new ContaFidelidade();
            conta.setCliente(cliente);
            contaFidelidadeRepository.save(conta);
        }
        return cliente;
    }

    // RF14: consentimento pode ser dado depois do cadastro.
    @Transactional
    public Cliente registrarConsentimento(Long clienteId) {
        Cliente cliente = buscarInterno(clienteId);
        if (!cliente.isConsentimentoLgpd()) {
            cliente.setConsentimentoLgpd(true);
            cliente.setDataConsentimento(LocalDateTime.now());
            if (contaFidelidadeRepository.findByClienteId(clienteId).isEmpty()) {
                ContaFidelidade conta = new ContaFidelidade();
                conta.setCliente(cliente);
                contaFidelidadeRepository.save(conta);
            }
        }
        return cliente;
    }

    // RF16: todo acesso a dados pessoais é auditado.
    @Transactional
    public Cliente consultar(Long clienteId, String autor) {
        Cliente cliente = buscarInterno(clienteId);
        auditoriaService.registrar(TipoOperacaoSensivel.ACESSO_DADOS_PESSOAIS, autor, "Consulta de dados pessoais do cliente " + clienteId, null, null, clienteId);
        return cliente;
    }

    // RF15: apaga os dados pessoais do cliente, mantendo os pedidos para relatórios (INF07).
    @Transactional
    public Cliente anonimizar(Long clienteId, String autor) {
        Cliente cliente = buscarInterno(clienteId);
        if (cliente.isAnonimizado()) {
            throw new NegocioException("Cliente já anonimizado");
        }
        cliente.anonimizar();
        auditoriaService.registrar(TipoOperacaoSensivel.ANONIMIZACAO, autor, "Anonimização dos dados do cliente " + clienteId, "Solicitação do titular (LGPD)", null, clienteId);
        return cliente;
    }

    // RF11/RF12/RF13: pontos, desconto progressivo e frequência de consumo.
    @Transactional(readOnly = true)
    public FidelidadeResp fidelidade(Long clienteId) {
        ContaFidelidade conta = contaFidelidadeRepository.findByClienteId(clienteId).orElseThrow(() -> new RecursoNaoEncontradoException("Cliente sem conta de fidelidade (é necessário consentimento LGPD): " + clienteId));
        long frequencia = pedidoRepository.frequenciaConsumo(clienteId);
        return new FidelidadeResp(clienteId, conta.getPontos(), conta.percentualDescontoProgressivo(), frequencia);
    }

    private Cliente buscarInterno(Long clienteId) {
        return clienteRepository.findById(clienteId).orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado: " + clienteId));
    }

    public ClienteService(ClienteRepository clienteRepository, ContaFidelidadeRepository contaFidelidadeRepository, PedidoRepository pedidoRepository, AuditoriaService auditoriaService) {
        this.clienteRepository = clienteRepository;
        this.contaFidelidadeRepository = contaFidelidadeRepository;
        this.pedidoRepository = pedidoRepository;
        this.auditoriaService = auditoriaService;
    }
}
