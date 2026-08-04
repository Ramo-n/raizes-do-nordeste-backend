## Arquitetura Proposta

### Visão geral

A solução é um **back-end único (API REST)** que atende todos os canais descritos no
estudo de caso — aplicativo oficial, totens de autoatendimento, atendimento de balcão e
pick-up (seção 2). Assim, todos os canais usam as mesmas regras de negócio e os mesmos
dados, e o cliente tem a mesma experiência em qualquer um deles.

Os quatro canais (aplicativo, totem, balcão e pick-up) fazem requisições à mesma API,
que é organizada em três camadas (Controller, Service e Repository) e usa um banco de
dados relacional. A API também se comunica com o serviço externo de pagamento (seção 5)
e grava os registros de auditoria no banco.

### Decisões e justificativas

| Decisão | Justificativa (estudo de caso) |
|---------|-------------------------------|
| API REST organizada em camadas (Controller, Service, Repository) | Separa as regras de negócio do restante do código, deixando o sistema mais organizado e fácil de manter (seção 7). |
| Uma única aplicação, dividida em módulos (cardápio, pedidos, estoque, fidelidade, pagamentos, auditoria, relatórios) | É a opção mais simples de desenvolver e manter para o porte atual da rede; no futuro, com o crescimento, os módulos podem virar serviços separados (seções 1 e 6). |
| Pagamento feito por serviço externo, através de uma interface (`PagamentoGateway`) | Seção 5: o sistema apenas solicita o pagamento e recebe o resultado. Usando uma interface, é possível trocar o provedor sem mexer no resto do código; na entrega acadêmica o provedor é simulado. |
| O resultado do pagamento chega por um endpoint de retorno (callback) protegido contra duplicação | Seção 5 pede cuidado no tratamento de falhas: se o serviço externo enviar o mesmo resultado duas vezes, o sistema percebe e não baixa o estoque nem soma pontos em dobro. |
| Registros de auditoria que só podem ser adicionados (nunca alterados ou apagados) | Seções 3 e 4: cancelamentos, descontos, ajustes e acessos a dados pessoais precisam ficar registrados com autor, data e justificativa. |
| Banco de dados relacional (H2 na entrega; PostgreSQL sugerido para produção) | Os dados são bem relacionados entre si (pedidos, itens, estoque por unidade) e operações como baixa de estoque precisam de transações (seções 3 e 5). |
| Dados de cartão nunca passam pelo sistema | Seção 5: o processamento é 100% externo — menos risco de segurança. |
| Perfis de acesso (cliente, atendente, gerente, matriz) | Seção 3 (equipes e matriz) + LGPD (seção 4): relatórios e auditoria não podem ser públicos. |

### Alta disponibilidade, escalabilidade e tolerância a falhas (seção 6)

- A API não guarda estado na memória entre requisições, então é possível rodar várias
  cópias dela ao mesmo tempo atrás de um balanceador de carga — nos horários de pico,
  basta adicionar mais cópias;
- Os relatórios da matriz podem ler de uma cópia do banco, para não atrapalhar a operação
  das lojas nos horários de pico;
- Se o serviço de pagamento falhar, o pedido fica como `AGUARDANDO_PAGAMENTO` e a
  solicitação pode ser feita de novo, sem corromper nenhum dado;
- As chamadas ao serviço externo têm limite de tempo (timeout) e podem ser repetidas em
  caso de erro;
- Os logs da aplicação e os registros de auditoria permitem rastrear o que aconteceu no
  sistema (seção 3).

### LGPD (seção 4)

- O consentimento do cliente é registrado com data e hora antes de qualquer uso dos dados
  para fidelidade ou campanhas;
- O cliente pode pedir a anonimização: seus dados pessoais são apagados de forma
  definitiva, mas os pedidos continuam existindo para os relatórios da matriz (sem
  identificar ninguém);
- Todo acesso a dados pessoais e toda anonimização geram um registro de auditoria;
- São guardados apenas os dados necessários: nome, e-mail e data de nascimento (usados
  pela fidelidade e pelas campanhas por idade/perfil/frequência — seção 4).

### Evolução futura

O crescimento da rede (seções 1 e 6) é suportado sem grandes mudanças: novos canais podem
consumir a mesma API e, se necessário, os módulos podem ser separados em serviços
independentes no futuro.
