-- Dados de exemplo baseados no estudo de caso (Recife = primeira unidade; produtos nordestinos; sazonais juninos)
INSERT INTO unidade (nome, cidade, regiao, tipo_cozinha, horario_funcionamento, ativa) VALUES
  ('Raízes Recife Centro', 'Recife', 'Nordeste', 'COMPLETA', '06:00-22:00', true),
  ('Raízes São Paulo Paulista', 'São Paulo', 'Sudeste', 'REDUZIDA', '06:30-21:00', true);

INSERT INTO produto (nome, descricao, categoria) VALUES
  ('Tapioca de queijo coalho', 'Tapioca recheada com queijo coalho', 'Tapiocas'),
  ('Cuscuz recheado', 'Cuscuz nordestino com recheio', 'Cuscuz'),
  ('Bolo de macaxeira', 'Bolo tradicional de macaxeira', 'Bolos'),
  ('Suco de cajá', 'Suco regional de cajá', 'Sucos'),
  ('Café da manhã completo', 'Café passado, cuscuz, ovos e manteiga de garrafa', 'Combos'),
  ('Canjica junina', 'Canjica típica do período junino', 'Sazonais');

-- Cardápio por unidade (preços/variações regionais; canjica sazonal junina: maio–julho)
INSERT INTO produto_unidade (produto_id, unidade_id, preco, disponivel, sazonal, mes_inicio_sazonalidade, mes_fim_sazonalidade, variacao_regional) VALUES
  (1, 1, 12.00, true, false, NULL, NULL, NULL),
  (2, 1, 10.00, true, false, NULL, NULL, NULL),
  (3, 1, 8.00, true, false, NULL, NULL, NULL),
  (4, 1, 7.00, true, false, NULL, NULL, NULL),
  (5, 1, 25.00, true, false, NULL, NULL, NULL),
  (6, 1, 9.00, true, true, 5, 7, NULL),
  (1, 2, 14.00, true, false, NULL, NULL, 'Queijo coalho levemente tostado'),
  (2, 2, 12.00, true, false, NULL, NULL, NULL),
  (4, 2, 9.00, true, false, NULL, NULL, NULL);

INSERT INTO estoque_item (unidade_id, produto_id, quantidade, quantidade_minima) VALUES
  (1, 1, 100, 10), (1, 2, 80, 10), (1, 3, 50, 5), (1, 4, 200, 20), (1, 5, 40, 5), (1, 6, 60, 10),
  (2, 1, 70, 10), (2, 2, 60, 10), (2, 4, 150, 20);

INSERT INTO cliente (nome, email, data_nascimento, consentimento_lgpd, data_consentimento, anonimizado) VALUES
  ('Maria das Dores', 'maria@exemplo.com', '1985-03-10', true, CURRENT_TIMESTAMP, false),
  ('João Severino', 'joao@exemplo.com', '1990-06-24', false, NULL, false);

INSERT INTO conta_fidelidade (cliente_id, pontos) VALUES (1, 120);

INSERT INTO meta_unidade (unidade_id, ano_mes, meta_vendas) VALUES
  (1, '2026-07', 50000.00),
  (2, '2026-07', 35000.00);
