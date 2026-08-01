# Rede "Raízes do Nordeste" — Back-end

API REST da rede de lanchonetes nordestinas **Raízes do Nordeste**, desenvolvida para a
disciplina **Projeto Multidisciplinar (Trilha Back-end)**, com base exclusiva no estudo de
caso oficial da disciplina.

## Sobre o projeto

A Raízes do Nordeste é uma franquia em expansão, com unidades em várias cidades. O sistema
é uma **API única multicanal**: aplicativo oficial, totens de autoatendimento, balcão e
retirada rápida (pick-up) consomem os mesmos endpoints, garantindo a mesma jornada em
qualquer canal.

Principais funcionalidades (todas rastreadas aos requisitos do estudo de caso):

| Módulo | O que faz |
|--------|-----------|
| Cardápio por unidade | Cada unidade tem seus produtos, preços, variações regionais e itens sazonais (ex.: período junino) |
| Pedidos multicanal | Criação, acompanhamento de status (`CRIADO → AGUARDANDO_PAGAMENTO → PAGO → EM_PREPARO → PRONTO → ENTREGUE`), cancelamento e desconto |
| Pagamentos desacoplados | O sistema apenas solicita o pagamento a um serviço externo e recebe o resultado por callback idempotente — nunca processa dados de cartão |
| Estoque local | Controle por unidade, baixa automática na confirmação do pagamento, devolução no cancelamento e ajuste manual auditado |
| Fidelidade + LGPD | Pontos por consumo, desconto progressivo, consentimento explícito, anonimização irreversível e auditoria de acessos a dados pessoais |
| Auditoria | Trilha imutável de operações sensíveis: cancelamentos, descontos, ajustes de estoque, acessos e anonimizações |
| Relatórios da matriz | Vendas por unidade/região, produtos mais consumidos e indicadores meta × realizado |

## Tecnologias

- Java 17 · Spring Boot 3 (Web, Data JPA, Validation)
- Banco H2 em memória (dados de exemplo carregados automaticamente)
- Documentação interativa com OpenAPI/Swagger (springdoc)
- Testes com JUnit 5 + AssertJ

## Estrutura do repositório

```
docs/
  01-requisitos.md          # Requisitos funcionais/não funcionais e inferências justificadas
  02-arquitetura.md         # Arquitetura proposta e decisões técnicas
  03-api.md                 # Documentação de todos os endpoints
  04-plano-de-testes.md     # Plano de testes (CT01–CT15)
  diagramas/                # Casos de uso, diagrama de classes e DER (PlantUML + PNG)
backend/
  src/main/java/...         # Código da API (domain, repository, service, web)
  src/test/java/...         # Testes automatizados
```

## Como rodar

Pré-requisitos: **Java 17+** e **Maven**.

```bash
cd backend
mvn spring-boot:run
```

Depois que aparecer `Started RaizesApplication`:

- Swagger UI (testar a API pelo navegador): http://localhost:8080/swagger-ui.html
- Console do banco H2: http://localhost:8080/h2-console
  (JDBC URL `jdbc:h2:mem:raizes`, usuário `sa`, senha em branco)

## Como testar

```bash
cd backend
mvn test
```

12 testes automatizados cobrem o fluxo de pedido/pagamento, estoque, fidelidade,
sazonalidade do cardápio, auditoria e LGPD.

## Fluxo principal (exemplo com curl ou Postman)

```bash
# 1. Ver o cardápio da unidade 1 (Recife)
curl http://localhost:8080/api/unidades/1/cardapio

# 2. Criar um pedido pelo aplicativo (cliente 1, que tem fidelidade)
curl -X POST http://localhost:8080/api/pedidos -H "Content-Type: application/json" \
  -d '{"unidadeId":1,"clienteId":1,"canal":"APLICATIVO","itens":[{"produtoId":1,"quantidade":2}]}'

# 3. Simular o callback do serviço externo confirmando o pagamento
curl -X POST http://localhost:8080/api/pagamentos/1/resultado -H "Content-Type: application/json" \
  -d '{"referenciaExterna":"EXT-abc","status":"CONFIRMADO"}'

# 4. Acompanhar o pedido (status PAGO, estoque baixado, pontos acumulados)
curl http://localhost:8080/api/pedidos/1

# 5. Relatório da matriz
curl http://localhost:8080/api/relatorios/vendas-por-unidade
```

## Documentação

- [Requisitos e análise do estudo de caso](docs/01-requisitos.md)
- [Arquitetura proposta](docs/02-arquitetura.md)
- [Documentação da API](docs/03-api.md)
- [Plano de testes](docs/04-plano-de-testes.md)
- Diagramas UML/DER em [docs/diagramas](docs/diagramas)

## Autor

Ramon — Projeto Multidisciplinar (Trilha Back-end).
