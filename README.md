# Rede "Raízes do Nordeste" — Back-end

Projeto Multidisciplinar (Trilha Back-end). Sistema da rede de lanchonetes nordestinas
"Raízes do Nordeste", desenvolvido exclusivamente a partir do estudo de caso oficial da
disciplina: API única multicanal (aplicativo, totem, balcão, pick-up), cardápio e estoque
por unidade, sazonalidade/variações regionais, pagamentos desacoplados em serviço externo,
programa de fidelidade com LGPD, auditoria de operações sensíveis e relatórios da matriz.

## Estrutura

```
docs/
  01-requisitos.md          # Etapas 1-4: leitura, requisitos, inferências, validação
  02-arquitetura.md         # Arquitetura proposta e decisões justificadas
  03-api.md                 # Documentação da API (também via Swagger UI)
  04-plano-de-testes.md     # Plano de testes (casos CT01–CT15)
  05-documentacao-final.pdf # Documentação final consolidada em PDF
  diagramas/                # Casos de uso, diagrama de classes e DER (PlantUML + PNG)
backend/                    # API REST Java 17 + Spring Boot 3
```

## Como executar

Pré-requisitos: Java 17 e Maven.

```bash
cd backend
mvn spring-boot:run
```

- Swagger UI: http://localhost:8080/swagger-ui.html
- Console H2 (banco em memória com dados de exemplo): http://localhost:8080/h2-console
  (JDBC URL `jdbc:h2:mem:raizes`, usuário `sa`, senha vazia)

## Testes

```bash
cd backend
mvn test
```

## Fluxo principal (exemplo)

```bash
# 1. Cardápio da unidade 1 (Recife)
curl http://localhost:8080/api/unidades/1/cardapio

# 2. Pedido pelo aplicativo (cliente 1, com fidelidade)
curl -X POST http://localhost:8080/api/pedidos -H 'Content-Type: application/json' \
  -d '{"unidadeId":1,"clienteId":1,"canal":"APLICATIVO","itens":[{"produtoId":1,"quantidade":2}]}'

# 3. Callback do provedor externo confirmando o pagamento
curl -X POST http://localhost:8080/api/pagamentos/1/resultado -H 'Content-Type: application/json' \
  -d '{"referenciaExterna":"EXT-abc","status":"CONFIRMADO"}'

# 4. Acompanhar o pedido
curl http://localhost:8080/api/pedidos/1
```
