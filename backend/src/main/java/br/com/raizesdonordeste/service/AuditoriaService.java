package br.com.raizesdonordeste.service;

import br.com.raizesdonordeste.domain.RegistroAuditoria;
import br.com.raizesdonordeste.domain.TipoOperacaoSensivel;
import br.com.raizesdonordeste.repository.RegistroAuditoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditoriaService {

    private final RegistroAuditoriaRepository repository;

    public RegistroAuditoria registrar(TipoOperacaoSensivel tipo, String autor, String detalhes,
                                       String justificativa, Long pedidoId, Long clienteId) {
        RegistroAuditoria r = new RegistroAuditoria();
        r.setTipo(tipo);
        r.setAutor(autor);
        r.setDetalhes(detalhes);
        r.setJustificativa(justificativa);
        r.setPedidoId(pedidoId);
        r.setClienteId(clienteId);
        return repository.save(r);
    }

    public List<RegistroAuditoria> listar(TipoOperacaoSensivel tipo) {
        return tipo == null ? repository.findAllByOrderByDataHoraDesc()
                : repository.findByTipoOrderByDataHoraDesc(tipo);
    }
}
