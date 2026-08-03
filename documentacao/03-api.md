## Documentação da API (principais endpoints)

A documentação interativa (OpenAPI/Swagger) é gerada automaticamente pelo back-end:

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

Todos os canais do estudo de caso (aplicativo, totem, balcão, pick-up) consomem esta mesma
API, indicando o canal no campo `canal` do pedido.

### Unidades e cardápio (RF01, RF05, RF19)

| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/api/unidades` | Lista as unidades da rede |
| GET | `/api/unidades/{id}` | Detalha uma unidade |
| POST | `/api/unidades` | Cadastra unidade (matriz) |
| GET | `/api/unidades/{id}/cardapio` | Cardápio da unidade — só produtos disponíveis no local e no período (sazonalidade junina, variações regionais) |

### Pedidos (RF02, RF03, RF04, RF07, RF12)

| Método | Rota | Descrição |
|--------|------|-----------|
| POST | `/api/pedidos` | Cria pedido de qualquer canal; valida cardápio/estoque da unidade, aplica desconto progressivo de fidelidade e **solicita o pagamento externo** |
| GET | `/api/pedidos/{id}` | Acompanha o pedido e seu status |
| GET | `/api/pedidos?unidadeId=` | Lista pedidos da unidade |
| POST | `/api/pedidos/{id}/avancar` | Avança status: PAGO → EM_PREPARO → PRONTO → ENTREGUE |
| POST | `/api/pedidos/{id}/cancelamento` | Cancela (operação sensível — auditada; devolve estoque se já pago) |
| POST | `/api/pedidos/{id}/desconto` | Desconto manual (operação sensível — auditada) |

Exemplo — criar pedido:

```json
POST /api/pedidos
{
  "unidadeId": 1,
  "clienteId": 1,
  "canal": "APLICATIVO",
  "itens": [ { "produtoId": 1, "quantidade": 2 } ]
}
```

Ciclo de vida do pedido (INF01):
`CRIADO → AGUARDANDO_PAGAMENTO → PAGO → EM_PREPARO → PRONTO → ENTREGUE`,
com desvios `CANCELADO` e `PAGAMENTO_RECUSADO`.

### Pagamentos (RF17, RF18)

| Método | Rota | Descrição |
|--------|------|-----------|
| POST | `/api/pagamentos/{pedidoId}/resultado` | Retorno do serviço externo com `CONFIRMADO` ou `RECUSADO`. Se o mesmo resultado for enviado duas vezes, o sistema ignora a repetição |

```json
POST /api/pagamentos/1/resultado
{ "referenciaExterna": "EXT-abc", "status": "CONFIRMADO" }
```

O sistema nunca recebe dados de cartão — apenas o resultado do processamento externo
(estudo de caso, seção 5).

### Clientes, fidelidade e LGPD (RF11–RF16)

| Método | Rota | Descrição |
|--------|------|-----------|
| POST | `/api/clientes` | Cadastro (com consentimento explícito opcional) |
| GET | `/api/clientes/{id}` | Consulta dados pessoais — exige header `X-Autor` e gera auditoria de acesso |
| POST | `/api/clientes/{id}/consentimento` | Registra consentimento explícito e abre a conta de fidelidade |
| POST | `/api/clientes/{id}/anonimizacao` | Anonimização definitiva dos dados do cliente (auditada) — exige header `X-Autor` |
| GET | `/api/clientes/{id}/fidelidade` | Pontos, percentual de desconto progressivo e frequência de consumo |

Regras de fidelidade (INF03): 1 ponto por R$1 em pedidos confirmados; desconto progressivo
por faixa — 100+ pontos: 5%; 500+: 10%; 1000+: 15%. Pontos e desconto só se aplicam a
clientes **com consentimento LGPD**.

### Estoque (RF06, RF07)

| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/api/estoque?unidadeId=` | Estoque local da unidade |
| POST | `/api/estoque/{itemId}/ajuste` | Ajuste manual (operação sensível — auditada) |

### Relatórios da matriz (RF08, RF09, RF10, RF20)

| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/api/relatorios/vendas-por-unidade` | Vendas consolidadas por unidade |
| GET | `/api/relatorios/vendas-por-regiao` | Vendas consolidadas por região |
| GET | `/api/relatorios/produtos-mais-consumidos` | Ranking de produtos |
| GET | `/api/relatorios/indicadores/{unidadeId}` | Metas × vendas realizadas por unidade |

### Auditoria (RF07, RF16)

| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/api/auditoria?tipo=` | Trilha de auditoria; filtro por tipo (`CANCELAMENTO`, `DESCONTO`, `AJUSTE_ESTOQUE`, `ACESSO_DADOS_PESSOAIS`, `ANONIMIZACAO`) |

### Erros

| HTTP | Situação |
|------|----------|
| 400 | Validação de entrada (Bean Validation) |
| 404 | Recurso não encontrado |
| 422 | Regra de negócio violada (produto fora do cardápio da unidade, estoque insuficiente, status inválido...) |
