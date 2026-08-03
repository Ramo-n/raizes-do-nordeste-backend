# Raízes do Nordeste — Back-end

Projeto da disciplina **Projeto Multidisciplinar (Trilha Back-end)**.

API REST para a rede de lanchonetes Raízes do Nordeste, feita com base no estudo de caso
da disciplina. A mesma API atende o aplicativo, os totens, o balcão e o pick-up.

## O que o sistema faz

- Mostra o cardápio de cada unidade (com produtos sazonais, como os juninos)
- Registra pedidos e acompanha o status até a entrega
- Pede o pagamento a um serviço externo e recebe a confirmação
- Controla o estoque de cada unidade
- Programa de fidelidade com pontos e descontos (com consentimento LGPD)
- Registra auditoria de cancelamentos, descontos e ajustes
- Relatórios de vendas para a matriz

## Tecnologias

- Java 17
- Spring Boot 3 (Web, JPA, Validation)
- Banco H2 em memória (já vem com dados de exemplo)
- Swagger para testar a API
- JUnit para os testes

## Como rodar

Precisa ter o Java 17 e o Maven instalados.

```bash
cd backend
mvn spring-boot:run
```

Quando aparecer `Started RaizesApplication`, abra no navegador:

- Swagger (testar a API): http://localhost:8080/swagger-ui.html

## Como rodar os testes

```bash
cd backend
mvn test
```

## Exemplo de uso

1. `GET /api/unidades/1/cardapio` — ver o cardápio da unidade 1
2. `POST /api/pedidos` — criar um pedido:
   ```json
   { "unidadeId": 1, "clienteId": 1, "canal": "APLICATIVO", "itens": [ { "produtoId": 1, "quantidade": 2 } ] }
   ```
3. `POST /api/pagamentos/1/resultado` — simular a confirmação do pagamento:
   ```json
   { "referenciaExterna": "EXT-abc", "status": "CONFIRMADO" }
   ```
4. `GET /api/pedidos/1` — ver o pedido pago

## Documentação

Na pasta [documentacao](documentacao) estão os requisitos, a arquitetura, a documentação da API,
o plano de testes e os diagramas (casos de uso, classes e DER).

## Autor

Ramon De Oliveira
