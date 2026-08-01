# Rede "Raízes do Nordeste" — Levantamento de Requisitos

> Documento produzido a partir da leitura integral do estudo de caso oficial da disciplina
> Projeto Multidisciplinar (Trilha Back-end). Todas as afirmações abaixo referenciam a
> seção do estudo de caso que as fundamenta.

## Etapa 1 — Leitura

O estudo de caso foi lido integralmente. Resumo do contexto (seções 1 a 7):

A Raízes do Nordeste é uma rede de lanchonetes de culinária nordestina, nascida em Recife
e em expansão para várias capitais e cidades do interior (seção 1). O cliente interage por
múltiplos canais — aplicativo oficial, totens de autoatendimento, balcão e retirada rápida
(pick-up) — e espera a mesma jornada em todos eles: ver o cardápio da unidade, selecionar
produtos disponíveis naquele local, pedir, acompanhar o status e receber corretamente
(seção 2). As unidades diferem entre si (cozinha completa ou reduzida, produtos sazonais
— especialmente juninos — e variações regionais de receita), diferenças que devem ser
controladas sem quebrar o padrão da franquia (seção 2). Cada unidade tem equipe própria,
estoque local, regras de funcionamento e metas (seção 3). A matriz acompanha vendas por
unidade/região, produtos mais consumidos, relatórios financeiros, dados consolidados e
auditoria de operações sensíveis — cancelamentos, descontos e ajustes (seção 3). Há um
programa de fidelização (pontos, descontos progressivos, campanhas segmentadas) com
conformidade estrita à LGPD: consentimento explícito, tratamento adequado, anonimização,
auditoria de acessos (seção 4). Pagamentos são processados por serviços externos: o
sistema apenas solicita, recebe confirmação/negativa, registra e atualiza o pedido
(seção 5). O sistema deve ser robusto, escalável, auditável, tolerante a falhas e de alta
disponibilidade (seção 6), com arquitetura coerente e sustentável (seção 7).

## Etapa 2 — Requisitos explícitos

### Requisitos Funcionais (RF)

| ID | Requisito | Fonte no estudo de caso |
|----|-----------|------------------------|
| RF01 | Exibir o cardápio da unidade selecionada, contendo apenas produtos disponíveis naquele local. | Seção 2 ("visualizar o cardápio da unidade"; "selecionar produtos disponíveis naquele local") |
| RF02 | Registrar pedidos originados de qualquer canal: aplicativo, totem, balcão (atendimento humano) e pick-up. | Seção 2 (lista de canais) |
| RF03 | Permitir pedidos antecipados pelo aplicativo. | Seção 2 ("Aplicativo oficial (pedidos antecipados...)") |
| RF04 | Permitir acompanhamento do status do pedido até a entrega/retirada. | Seção 2 ("acompanhar o status"; "receber o pedido corretamente") |
| RF05 | Controlar a disponibilidade de produtos por unidade, incluindo produtos sazonais (ex.: período junino) e variações regionais, sem quebrar o padrão da franquia. | Seção 2 (parágrafo final) |
| RF06 | Controlar estoque local por unidade. | Seção 3 ("controle de estoque local") |
| RF07 | Registrar auditoria de operações sensíveis: cancelamentos, descontos e ajustes. | Seção 3 ("auditoria de operações sensíveis (cancelamentos, descontos, ajustes)") |
| RF08 | Disponibilizar à matriz relatórios de vendas por unidade e por região. | Seção 3 ("vendas por unidade e por região") |
| RF09 | Disponibilizar relatório de produtos mais consumidos. | Seção 3 ("produtos mais consumidos") |
| RF10 | Disponibilizar relatórios financeiros e dados consolidados para decisões estratégicas. | Seção 3 |
| RF11 | Programa de fidelidade: acumular pontos por consumo. | Seção 4 ("acumular pontos") |
| RF12 | Programa de fidelidade: oferecer descontos progressivos. | Seção 4 ("descontos progressivos") |
| RF13 | Programa de fidelidade: permitir campanhas segmentadas considerando frequência de consumo, idade e perfil do cliente. | Seção 4 |
| RF14 | Registrar consentimento explícito do cliente para uso de seus dados (opt-in). | Seção 4 ("consentimento explícito para uso de dados") |
| RF15 | Permitir anonimização de dados pessoais quando aplicável. | Seção 4 ("anonimização quando aplicável") |
| RF16 | Registrar auditoria de acessos a dados pessoais. | Seção 4 ("auditoria de acessos") |
| RF17 | Solicitar pagamento a serviço externo, receber confirmação ou negativa, registrar o resultado e atualizar o status do pedido. | Seção 5 (lista completa) |
| RF18 | Tratar falhas na integração de pagamento sem corromper o estado do pedido. | Seção 5 ("exige cuidado na modelagem e no tratamento de falhas") |
| RF19 | Cadastrar e gerenciar unidades da franquia com suas características (formato de cozinha, regras de funcionamento). | Seção 2/3 ("Nem todas as unidades são idênticas"; "regras de funcionamento específicas") |
| RF20 | Acompanhar metas e indicadores de desempenho por unidade. | Seção 3 ("metas e indicadores de desempenho") |

### Requisitos Não Funcionais (RNF)

| ID | Requisito | Fonte |
|----|-----------|-------|
| RNF01 | Alta disponibilidade, principalmente em horários de pico. | Seção 6 |
| RNF02 | Escalabilidade para suportar o crescimento contínuo da rede. | Seções 1, 3, 6 |
| RNF03 | Tolerância a falhas (inclusive falhas do serviço externo de pagamento). | Seções 5, 6 |
| RNF04 | Auditabilidade: rastreabilidade e transparência das operações. | Seções 3, 6 |
| RNF05 | Conformidade com a LGPD (consentimento, minimização, anonimização, auditoria de acesso). | Seção 4 |
| RNF06 | Segurança: pagamentos desacoplados; o sistema não processa nem armazena dados de cartão. | Seção 5 |
| RNF07 | Padronização entre unidades preservando variações controladas (sazonais/regionais). | Seção 2 |
| RNF08 | Arquitetura coerente, sustentável no longo prazo e baseada em integração de sistemas. | Seções 5, 7 |
| RNF09 | Consistência multicanal: a mesma jornada em app, totem, balcão e pick-up. | Seção 2 |

## Etapa 3 — Inferências mínimas (justificadas)

Somente inferências indispensáveis para o sistema funcionar, cada uma ancorada no texto:

| ID | Inferência | Justificativa |
|----|-----------|---------------|
| INF01 | O ciclo de vida do pedido possui estados: `CRIADO → AGUARDANDO_PAGAMENTO → PAGO → EM_PREPARO → PRONTO → ENTREGUE`, com `CANCELADO` e `PAGAMENTO_RECUSADO` como desvios. | O estudo exige "acompanhar o status" (seção 2) e atualização de status conforme resultado do pagamento (seção 5); cancelamento é citado como operação sensível auditável (seção 3). Estados mínimos para cobrir essas exigências. |
| INF02 | Existe cadastro de cliente vinculado ao programa de fidelidade, com dados mínimos (nome, e-mail, data de nascimento) e flag de consentimento. | A fidelização considera "frequência de consumo, idade e perfil" (seção 4) — exige identificar o cliente e sua data de nascimento; a LGPD exige consentimento registrado. Pedidos de balcão/totem podem ser anônimos (nada no estudo obriga identificação). |
| INF03 | A regra de desconto progressivo é parametrizada por faixas de pontos acumulados. | "Descontos progressivos" (seção 4) exige alguma progressão; faixas de pontos é a materialização mínima, sem inventar regras adicionais. Valores das faixas são configuráveis. |
| INF04 | A baixa de estoque ocorre quando o pedido é confirmado (pago), por unidade. | "Controle de estoque local" (seção 3) só faz sentido se o consumo dos pedidos abater o estoque da unidade. |
| INF05 | A integração de pagamento é assíncrona: o sistema solicita e recebe o resultado por callback/consulta (gateway simulado na implementação). | Seção 5 descreve exatamente esse fluxo (solicita → recebe confirmação/negativa → registra → atualiza), e afirma que o processamento é externo. O gateway real não faz parte do escopo. |
| INF06 | Perfis de acesso mínimos: CLIENTE, ATENDENTE, GERENTE (unidade) e MATRIZ. | Seção 3 cita equipes (atendentes, cozinheiros, gerentes) e necessidades exclusivas da matriz (relatórios consolidados, auditoria); relatórios e auditoria não podem ser públicos (rastreabilidade/transparência, LGPD). |
| INF07 | A anonimização substitui dados pessoais identificáveis por valores irreversíveis, preservando registros de pedidos para relatórios. | Seção 4 pede anonimização "quando aplicável" e a matriz continua precisando de dados consolidados (seção 3). |
| INF08 | Auditoria é registrada em trilha imutável (append-only) com autor, operação, data e justificativa. | "Rastreabilidade e transparência" (seção 3) e "auditoria de acessos" (seção 4) exigem esses atributos mínimos. |


## Etapa 4 — Validação

Checklist de aderência: cada funcionalidade implementada rastreia para um RF/RNF acima, e
cada RF/RNF rastreia para uma seção do estudo de caso. Funcionalidades deliberadamente
**fora do escopo** por não terem respaldo no estudo de caso:

- Delivery/entrega em domicílio (o estudo cita apenas pick-up e consumo em loja);
- Processamento/armazenamento de dados de cartão (explicitamente delegado a terceiros);
- Gestão de folha de pagamento/RH das equipes;
- Cadastro de fornecedores e compras (o estudo cita apenas o controle do estoque local);
- App mobile e interface de totem em si (trilha Back-end: o sistema expõe a API que
  atende todos os canais).

Nenhuma funcionalidade além das listadas em RF/RNF/INF foi incluída na modelagem ou na
implementação.
