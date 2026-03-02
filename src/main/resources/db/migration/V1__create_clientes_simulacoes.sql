CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE clientes (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    nome VARCHAR(255) NOT NULL,
    salario_bruto DECIMAL(12,2),
    salario_liquido DECIMAL(12,2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE simulacoes (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    cliente_id UUID NOT NULL REFERENCES clientes(id) ON DELETE CASCADE,
    valor_solicitado DECIMAL(12,2) NOT NULL,
    prazo_meses INTEGER NOT NULL,
    score INTEGER,
    resultado VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);