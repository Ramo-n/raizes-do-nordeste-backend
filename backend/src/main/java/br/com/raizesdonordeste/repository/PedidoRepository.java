package br.com.raizesdonordeste.repository;

import br.com.raizesdonordeste.domain.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByUnidadeId(Long unidadeId);
    List<Pedido> findByClienteId(Long clienteId);

    @Query("""
            select p.unidade.id, p.unidade.nome, p.unidade.regiao, count(p), sum(p.valorTotal)
            from Pedido p
            where p.status in (br.com.raizesdonordeste.domain.StatusPedido.PAGO,
                               br.com.raizesdonordeste.domain.StatusPedido.EM_PREPARO,
                               br.com.raizesdonordeste.domain.StatusPedido.PRONTO,
                               br.com.raizesdonordeste.domain.StatusPedido.ENTREGUE)
            group by p.unidade.id, p.unidade.nome, p.unidade.regiao
            """)
    List<Object[]> vendasPorUnidade();

    @Query("""
            select p.unidade.regiao, count(p), sum(p.valorTotal)
            from Pedido p
            where p.status in (br.com.raizesdonordeste.domain.StatusPedido.PAGO,
                               br.com.raizesdonordeste.domain.StatusPedido.EM_PREPARO,
                               br.com.raizesdonordeste.domain.StatusPedido.PRONTO,
                               br.com.raizesdonordeste.domain.StatusPedido.ENTREGUE)
            group by p.unidade.regiao
            """)
    List<Object[]> vendasPorRegiao();

    @Query("""
            select i.produto.id, i.produto.nome, sum(i.quantidade)
            from ItemPedido i
            where i.pedido.status in (br.com.raizesdonordeste.domain.StatusPedido.PAGO,
                                      br.com.raizesdonordeste.domain.StatusPedido.EM_PREPARO,
                                      br.com.raizesdonordeste.domain.StatusPedido.PRONTO,
                                      br.com.raizesdonordeste.domain.StatusPedido.ENTREGUE)
            group by i.produto.id, i.produto.nome
            order by sum(i.quantidade) desc
            """)
    List<Object[]> produtosMaisConsumidos();

    @Query("""
            select count(p)
            from Pedido p
            where p.cliente.id = :clienteId
              and p.status in (br.com.raizesdonordeste.domain.StatusPedido.PAGO,
                               br.com.raizesdonordeste.domain.StatusPedido.EM_PREPARO,
                               br.com.raizesdonordeste.domain.StatusPedido.PRONTO,
                               br.com.raizesdonordeste.domain.StatusPedido.ENTREGUE)
            """)
    long frequenciaConsumo(@Param("clienteId") Long clienteId);
}
