CREATE TABLE cliente (
	id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    telefone VARCHAR(20),
    endereco VARCHAR(255),
    data_nascimento date
);
CREATE TABLE produto (
	id INT AUTO_INCREMENT PRIMARY KEY,
    descricao VARCHAR(100) NOT NULL,
    valor DECIMAL(20, 2) NOT NULL,
    quantidade_estoque INT NOT NULL
);
CREATE TABLE venda (
	id INT AUTO_INCREMENT PRIMARY KEY,
    data_compra DATE NOT NULL,
    valor_total DECIMAL(10, 2),
    cliente_id INT,
	FOREIGN KEY (cliente_id) REFERENCES cliente(id)
);
CREATE TABLE item_venda (
	id INT AUTO_INCREMENT PRIMARY KEY,
    venda_id INT,
    produto_id INT,
    quantidade INT NOT NULL,
    preco_unitario DECIMAL (10,2) NOT NULL,
    FOREIGN KEY (venda_id) REFERENCES venda(id),
	FOREIGN KEY (produto_id) REFERENCES produto(id)
);