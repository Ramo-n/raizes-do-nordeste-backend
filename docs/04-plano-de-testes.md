# Plano de Testes — Raízes do Nordeste (Back-end)

## Objetivo

Verificar que o back-end atende aos requisitos funcionais levantados no estudo de caso,
com foco nas regras de negócio críticas: cardápio por unidade, ciclo do pedido, integração
de pagamento, estoque, fidelidade, auditoria e LGPD.

## Estratégia

| Nível | Abordagem | Ferramenta |
|-------|-----------|-----------|
| Integração (serviço + banco) | Testes automatizados `@SpringBootTest` com H2 em memória e massa de dados do `data.sql` | JUnit 5 + AssertJ |
| API (manual/exploratório) | Execução dos endpoints via Swagger UI seguindo os casos abaixo | Swagger UI / curl |

Execução automatizada: `cd backend && mvn test`.

## Casos de teste

| ID | Requisito | Cenário | Resultado esperado | Automação |
|----|-----------|---------|--------------------|-----------|
| CT01 | RF01/RF05 | Consultar cardápio da unidade reduzida (SP) | Não lista produtos exclusivos de Recife (bolo de macaxeira, combo) | `LgpdECardapioTest.cardapioDaUnidadeSoTrazProdutosDoLocalERespeitaSazonalidade` |
| CT02 | RF05 | Consultar cardápio fora/dentro do período junino | Canjica junina só aparece entre maio e julho | idem CT01 |
| CT03 | RF02/RF12/RF17 | Criar pedido de cliente com 120 pontos (2 tapiocas, R$24) | Pedido `AGUARDANDO_PAGAMENTO`, desconto 5% (R$1,20), pagamento solicitado | `FluxoPedidoTest.criaPedidoComDescontoProgressivoESolicitaPagamento` |
| CT04 | RF17/RF06/RF11 | Callback confirma o pagamento | Pedido `PAGO`, estoque baixado, pontos acumulados | `FluxoPedidoTest.confirmacaoDePagamentoBaixaEstoqueEAcumulaPontos` |
| CT05 | RF18 | Callback reentregue (duplicado) | Nenhum efeito adicional (idempotência) | `FluxoPedidoTest.callbackDePagamentoEIdempotente` |
| CT06 | RF17/RF18 | Callback recusa o pagamento | Pedido `PAGAMENTO_RECUSADO`, estoque intacto | `FluxoPedidoTest.pagamentoRecusadoNaoBaixaEstoque` |
| CT07 | RF07 | Cancelar pedido pago | Pedido `CANCELADO`, estoque devolvido, registro de auditoria com autor/justificativa | `FluxoPedidoTest.cancelamentoDevolveEstoqueEGeraAuditoria` |
| CT08 | RF07 | Aplicar desconto manual | Total recalculado e auditoria de DESCONTO gerada | `FluxoPedidoTest.descontoManualEAuditado` |
| CT09 | RF01 | Pedir produto fora do cardápio da unidade | Erro 422 de negócio | `FluxoPedidoTest.produtoForaDoCardapioDaUnidadeERejeitado` |
| CT10 | RF06 | Pedir quantidade acima do estoque | Erro 422 "Estoque insuficiente" | `FluxoPedidoTest.estoqueInsuficienteImpedePedido` |
| CT11 | RF14/RF11 | Registrar consentimento explícito | Consentimento com data/hora e conta de fidelidade criada | `LgpdECardapioTest.consentimentoExplicitoAbreContaDeFidelidade` |
| CT12 | RF16 | Consultar dados pessoais de um cliente | Registro de auditoria `ACESSO_DADOS_PESSOAIS` | `LgpdECardapioTest.acessoADadosPessoaisEAuditado` |
| CT13 | RF15 | Anonimizar cliente | Nome/e-mail/data de nascimento removidos irreversivelmente; auditoria `ANONIMIZACAO` | `LgpdECardapioTest.anonimizacaoRemoveDadosPessoaisEEAuditada` |
| CT14 | RF04 | Avançar status do pedido pago | PAGO → EM_PREPARO → PRONTO → ENTREGUE; transições inválidas retornam 422 | manual (Swagger) |
| CT15 | RF08/RF09/RF10/RF20 | Consultar relatórios após pedidos confirmados | Vendas por unidade/região, ranking de produtos e indicador meta × realizado coerentes | manual (Swagger) |

## Critérios de aceite

- 100% dos testes automatizados passando (`mvn test`);
- casos manuais CT14–CT15 executados via Swagger com evidências;
- nenhuma operação sensível (cancelamento, desconto, ajuste, acesso a dados pessoais,
  anonimização) sem registro correspondente na trilha de auditoria.
