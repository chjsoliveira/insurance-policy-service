CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Tabela principal de solicitações de apólice
CREATE TABLE insurance_requests (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    customer_id UUID NOT NULL,
    product_id UUID NOT NULL,
    category VARCHAR(50) NOT NULL,
    sales_channel VARCHAR(50) NOT NULL,
    payment_method VARCHAR(50) NOT NULL,
    total_monthly_premium_amount DECIMAL(10,2) NOT NULL,
    insured_amount DECIMAL(12,2) NOT NULL,
    status VARCHAR(20) NOT NULL,
	payment_confirmed BOOLEAN DEFAULT FALSE,
    subscription_authorized BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    finished_at TIMESTAMP
);

-- Coberturas da apólice
CREATE TABLE insurance_request_coverages (
    id SERIAL PRIMARY KEY,
    insurance_request_id UUID NOT NULL REFERENCES insurance_requests(id) ON DELETE CASCADE,
    name VARCHAR(100) NOT NULL,
    amount DECIMAL(12,2) NOT NULL
);

-- Assistências da apólice
CREATE TABLE insurance_request_assistances (
    id SERIAL PRIMARY KEY,
    insurance_request_id UUID NOT NULL REFERENCES insurance_requests(id) ON DELETE CASCADE,
    name VARCHAR(100) NOT NULL
);

-- Histórico de mudanças de status
CREATE TABLE insurance_request_history (
    id SERIAL PRIMARY KEY,
    insurance_request_id UUID NOT NULL REFERENCES insurance_requests(id) ON DELETE CASCADE,
    status VARCHAR(20) NOT NULL,
    timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Análise de fraude
CREATE TABLE fraud_analysis (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    insurance_request_id UUID REFERENCES insurance_requests(id),
    customer_id UUID NOT NULL,
    analyzed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    classification VARCHAR(50) NOT NULL
);

-- Ocorrências relacionadas à fraude
CREATE TABLE fraud_occurrences (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    fraud_analysis_id UUID NOT NULL REFERENCES fraud_analysis(id) ON DELETE CASCADE,
    product_id BIGINT NOT NULL,
    type VARCHAR(50) NOT NULL,
    description TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

