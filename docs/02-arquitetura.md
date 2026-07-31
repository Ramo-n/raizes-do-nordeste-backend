# Arquitetura Proposta — Rede "Raízes do Nordeste"

## Visão geral

A solução é um **back-end único (API REST)** que atende todos os canais descritos no
estudo de caso — aplicativo oficial, totens de autoatendimento, atendimento de balcão e
pick-up (seção 2) — garantindo a **consistência multicanal** (RNF09): todos os canais
consomem as mesmas regras de negócio e os mesmos dados.

```
 App oficial   Totem   Balcão/PDV   Pick-up
      \          |         |          /
       \         |         |         /
        +----------------------------+
        |      API REST (HTTPS)      |
        |  Spring Boot — camadas:    |
        |  Controller → Service →    |
        |  Repository (JPA)          |
        +----------------------------+
           |            |         \
           |            |          \-> Serviço externo de
        Banco de     Trilha de         pagamento (integração
        dados        auditoria         assíncrona, seção 5)
        relacional   (append-only)
```

## Estilo arquitetural e decisões

| Decisão | Justificativa (estudo de caso) |
|---------|-------------------------------|
| API REST stateless em camadas (Controller/Service/Repository) | Statelessness permite replicação horizontal → escalabilidade e alta disponibilidade (seção 6). Camadas separam regras de negócio (seção 7: "modele dados, processos e regras de negócio"). |
| Monólito modular (módulos: catálogo, pedidos, estoque, fidelidade, pagamentos, auditoria, relatórios, LGPD) | Coerente e sustentável (seção 7) para o porte atual; os módulos têm fronteiras claras que permitem extração futura para microsserviços conforme o crescimento da rede (seções 1 e 6), sem custo prematuro de operação distribuída. |
| Pagamento **desacoplado** via porta/adaptador (`PagamentoGateway`) | Seção 5: o sistema apenas solicita, recebe o resultado, registra e atualiza o pedido. O adaptador real (gateway) fica fora do núcleo; na entrega acadêmica é simulado. |
| Confirmação de pagamento por endpoint de callback (webhook) idempotente | Seção 5 descreve fluxo assíncrono (confirmação/negativa chega depois); idempotência é o tratamento de falhas exigido (reentrega do callback não duplica efeito). |
| Trilha de auditoria append-only para operações sensíveis e acessos a dados pessoais | Seções 3 e 4 (cancelamentos, descontos, ajustes; auditoria de acessos; rastreabilidade e transparência). |
| Banco relacional (H2 em dev/entrega; PostgreSQL em produção) | Dados fortemente relacionais (pedidos, itens, estoque por unidade) e necessidade de transações ACID para baixa de estoque e pagamento (seções 3 e 5). |
| Dados de cartão nunca transitam pelo sistema | Seção 5: processamento é 100% externo — reduz risco e escopo de segurança. |
| Perfis de acesso (CLIENTE, ATENDENTE, GERENTE, MATRIZ) | Seção 3 (equipes e matriz) + LGPD (seção 4): relatórios e auditoria restritos. |

## Alta disponibilidade, escalabilidade e tolerância a falhas (seção 6)

- **Stateless + réplicas atrás de load balancer**: qualquer instância atende qualquer
  requisição; picos de demanda são absorvidos adicionando réplicas (scale-out).
- **Banco com réplica de leitura**: relatórios da matriz (seção 3) leem da réplica, sem
  concorrer com a operação das lojas nos horários de pico.
- **Falha do serviço de pagamento**: o pedido permanece `AGUARDANDO_PAGAMENTO`; a
  solicitação pode ser reenviada e o callback é idempotente — nenhum estado é corrompido
  (seção 5, "tratamento de falhas").
- **Timeouts e retries com backoff** nas chamadas externas; nenhuma chamada externa dentro
  de transação de banco.
- **Observabilidade**: logs estruturados + trilha de auditoria dão rastreabilidade
  (seção 3).

## LGPD (seção 4)

- Consentimento explícito registrado com data/hora (opt-in) antes de qualquer uso para
  fidelidade/campanhas.
- Anonimização irreversível sob demanda, preservando os pedidos para os relatórios
  consolidados da matriz (dados deixam de ser pessoais).
- Todo acesso a dados pessoais e toda anonimização geram registro de auditoria.
- Minimização: armazenam-se apenas nome, e-mail e data de nascimento (necessários a
  fidelidade e campanhas por idade/perfil/frequência — seção 4).

## Evolução

O crescimento da rede (seções 1 e 6) é suportado sem redesenho: novos canais consomem a
mesma API; módulos podem ser extraídos para serviços independentes; o particionamento
natural dos dados por `unidade_id` facilita sharding futuro.
