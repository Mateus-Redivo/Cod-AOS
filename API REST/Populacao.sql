-- Inserir Categorias
INSERT INTO categoria (nome, descricao) VALUES 
('Eletrônicos', 'Produtos eletrônicos em geral'),
('Informática', 'Equipamentos e acessórios para computador'),
('Smartphones', 'Telefones celulares e acessórios'),
('Games', 'Jogos e consoles'),
('Audio', 'Equipamentos de som e áudio');

-- Inserir Produtos com suas respectivas categorias
INSERT INTO produto (nome, descricao, preco, quantidade, categoria_id) VALUES 
-- Eletrônicos
('Smart TV 55"', 'Smart TV LED 4K 55 polegadas', 3299.90, 15, 1),
('Soundbar 2.1', 'Soundbar com subwoofer wireless', 899.90, 20, 1),
('Ar Condicionado Split', 'Ar Condicionado Split 12000 BTUs', 2199.90, 10, 1),

-- Informática
('Notebook Gamer', 'Notebook com processador i7 e placa dedicada', 5999.90, 8, 2),
('Mouse Gamer', 'Mouse RGB 16000 DPI', 299.90, 30, 2),
('SSD 1TB', 'SSD NVME 1TB', 599.90, 25, 2),

-- Smartphones
('iPhone 15', 'iPhone 15 128GB', 5999.90, 12, 3),
('Galaxy S23', 'Samsung Galaxy S23 256GB', 4999.90, 15, 3),
('Carregador Wireless', 'Carregador wireless 15W', 199.90, 40, 3),

-- Games
('PlayStation 5', 'Console PlayStation 5 Digital', 3999.90, 10, 4),
('Xbox Series X', 'Console Xbox Series X', 4299.90, 8, 4),
('Nintendo Switch', 'Console Nintendo Switch OLED', 2499.90, 12, 4),

-- Audio
('Fone Bluetooth', 'Fone de ouvido bluetooth com cancelamento de ruído', 899.90, 25, 5),
('Caixa de Som Portátil', 'Caixa de som bluetooth à prova d''água', 399.90, 30, 5),
('Microfone Condensador', 'Microfone condensador USB', 599.90, 18, 5);