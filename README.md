# Mercado das Pulgas

CREATE DATABASE MercadoDePulgas;
GO
USE MercadoDePulgas;
GO

CREATE TABLE Moedas(
IdMoeda INT PRIMARY KEY IDENTITY(1,1),
Nome VARCHAR(25) NOT NULL,
Ativo BIT NOT NULL DEFAULT 1
);

INSERT INTO Moedas (nome, ativo) VALUES('Ouro Real', 1);
INSERT INTO Moedas (nome, ativo) VALUES('Tibar', 1);

CREATE TABLE Reinos(
IdReino INT PRIMARY KEY IDENTITY(1,1),
Nome VARCHAR(25) NOT NULL,
Ativo BIT NOT NULL DEFAULT 1
);

INSERT INTO Reinos (nome, ativo) VALUES('SRM', 1);

CREATE TABLE Produtos(
IdProduto INT PRIMARY KEY IDENTITY(1,1),
Descricao VARCHAR(100) NOT NULL,
Valor NUMERIC(10,2) NOT NULL,
IdMoeda INT NOT NULL,
IdReino INT NOT NULL,
Ativo BIT NOT NULL DEFAULT 1,

CONSTRAINT FK_Moeda_Produto FOREIGN KEY (IdMoeda) REFERENCES Moedas(IdMoeda),
CONSTRAINT FK_Reino_Produto FOREIGN KEY (IdReino) REFERENCES Reinos(IdReino)
);

CREATE TABLE TaxaMoeda(
IdTaxaMoeda INT PRIMARY KEY IDENTITY(1,1),
Data DATETIME NOT NULL,
IdMoedaOrigem INT NOT NULL,
Taxa NUMERIC(10,2) NOT NULL,
IdMoedaDestino INT NOT NULL,
IdProduto INT,
Operacao INT NOT NULL,

CONSTRAINT FK_MoedaOrigem_Taxa FOREIGN KEY (IdMoedaOrigem) REFERENCES Moedas(IdMoeda),
CONSTRAINT FK_MoedaDestino_Taxa FOREIGN KEY (IdMoedaDestino) REFERENCES Moedas(IdMoeda),
CONSTRAINT FK_Produto_Taxa FOREIGN KEY (IdProduto) REFERENCES Produtos(IdProduto)
);

INSERT INTO TaxaMoeda (Data, IdMoedaOrigem, Taxa, IdMoedaDestino, Operacao) VALUES(GETDATE(), 1, '2.5', 2, 0);
INSERT INTO TaxaMoeda (Data, IdMoedaOrigem, Taxa, IdMoedaDestino, Operacao) VALUES(GETDATE(), 2, '2.5', 1, 1);

CREATE TABLE Transacoes(
IdTransacao INT PRIMARY KEY IDENTITY(1,1),
DataTransacao DATETIME NOT NULL,
TipoTransacao INT NOT NULL,
IdProduto INT,
IdReino INT NOT NULL,
IdMoedaOrigem INT NOT NULL,
IdMoedaDestino INT NOT NULL,
Quantidade NUMERIC(10,2) NOT NULL,
ValorTransacao NUMERIC(10,2) NOT NULL,
ValorFinalTransacao NUMERIC(10,2) NOT NULL,

CONSTRAINT FK_Produto_Transacao FOREIGN KEY (IdProduto) REFERENCES Produtos(IdProduto),
CONSTRAINT FK_Reino_Transacao FOREIGN KEY (IdReino) REFERENCES Reinos(IdReino),
CONSTRAINT FK_MoedaOrigem_Transacao FOREIGN KEY (IdMoedaOrigem) REFERENCES Moedas(IdMoeda),
CONSTRAINT FK_MoedaDestino_Transacao FOREIGN KEY (IdMoedaDestino) REFERENCES Moedas(IdMoeda)
);
