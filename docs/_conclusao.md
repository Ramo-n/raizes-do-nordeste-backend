# Conclusão

O desenvolvimento deste projeto permitiu aplicar, de forma integrada, os conceitos de
Engenharia de Software estudados ao longo do curso: levantamento e análise de requisitos a
partir de um cenário com ambiguidades, modelagem UML e de dados, definição de arquitetura
e implementação de uma API REST funcional com testes automatizados.

## Principais lições aprendidas

- Ler o problema antes de codificar: as decisões mais importantes (ciclo do pedido,
  cardápio por unidade, pagamento externo) vieram da interpretação do estudo de caso, não
  da tecnologia;
- Delimitar o escopo é tão importante quanto implementar: funcionalidades sem respaldo no
  estudo de caso (delivery, processamento de cartão, RH) foram deliberadamente excluídas;
- Integrações externas exigem cuidado com falhas: o callback de pagamento precisou ser
  idempotente para suportar reenvios sem duplicar efeitos.

## Desafios

- Traduzir requisitos implícitos (alta disponibilidade, LGPD) em decisões concretas de
  projeto e código;
- Equilibrar a padronização da franquia com as variações por unidade (cardápio,
  sazonalidade, receitas regionais).

## Pontos de atenção para evoluções futuras

- Migrar o banco H2 (usado para demonstração) para PostgreSQL em produção;
- Adicionar autenticação/autorização por perfil (cliente, atendente, gerente, matriz);
- Integrar um gateway de pagamento real no lugar do simulado;
- Evoluir os relatórios da matriz com filtros por período e exportação.

# Referências

- Estudo de caso "Rede Raízes do Nordeste — Tecnologia, Tradição e Escala", disciplina
  Projeto Multidisciplinar (roteiro oficial da atividade).
- SOMMERVILLE, Ian. **Engenharia de Software**. 10. ed. São Paulo: Pearson, 2018.
- Documentação oficial do Spring Boot. Disponível em: https://spring.io/projects/spring-boot
- Documentação oficial do Spring Data JPA. Disponível em: https://spring.io/projects/spring-data-jpa
- Especificação OpenAPI / Springdoc. Disponível em: https://springdoc.org
- PlantUML — ferramenta de diagramação. Disponível em: https://plantuml.com
- BRASIL. Lei nº 13.709/2018 (Lei Geral de Proteção de Dados Pessoais — LGPD).
  Disponível em: https://www.planalto.gov.br/ccivil_03/_ato2015-2018/2018/lei/l13709.htm
