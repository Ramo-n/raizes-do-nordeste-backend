# 4. Entrega Técnica

## Repositório

Todo o código-fonte, a documentação e os diagramas estão versionados em:

**https://github.com/Ramo-n/raizes-do-nordeste-backend**

## Artefatos entregues

| Artefato | Localização |
|----------|-------------|
| Código do back-end (Java 17 + Spring Boot 3) | `backend/src/main/java` |
| Testes automatizados (12 testes, JUnit 5) | `backend/src/test/java` |
| Massa de dados de exemplo | `backend/src/main/resources/data.sql` |
| Levantamento de requisitos | `docs/01-requisitos.md` |
| Arquitetura proposta | `docs/02-arquitetura.md` |
| Documentação da API | `docs/03-api.md` |
| Plano de testes | `docs/04-plano-de-testes.md` |
| Diagramas (casos de uso, classes, DER) | `docs/diagramas/` |

## Como executar a demonstração

1. Clonar o repositório e entrar na pasta `backend`;
2. Executar `mvn spring-boot:run` (requer Java 17 e Maven);
3. Acessar a documentação interativa da API em `http://localhost:8080/swagger-ui.html`;
4. Executar os testes automatizados com `mvn test`.

## Evidências

- 12 testes automatizados passando (`mvn test`): fluxo de pedido, pagamento, estoque,
  fidelidade, sazonalidade do cardápio, auditoria e LGPD;
- API documentada automaticamente via OpenAPI/Swagger;
- Trilha de auditoria consultável em `GET /api/auditoria`.
