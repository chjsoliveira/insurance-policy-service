
# Insurance Policy Service

Microsserviço desenvolvido para gerenciar o ciclo de vida de solicitações de apólices de seguros, seguindo arquitetura orientada a eventos (EDA) e práticas de desenvolvimento (DDD, SOLID, Clean Architecture).

## ✨ Visão Geral

Este serviço realiza:
- Criação de solicitações de apólice via API REST
- Integração com API externa de fraudes (mockada com WireMock)
- Processamento de eventos de pagamento e subscrição via Kafka
- Validação de risco com base no tipo de cliente e categoria da apólice
- Transição de estados conforme ciclo de vida (RECEIVED → VALIDATED → PENDING → APPROVED/REJECTED/CANCELLED)
- Publicação de eventos Kafka após cada transição de estado
- Observabilidade com Prometheus + Grafana

---

## ⚙️ Tecnologias Utilizadas

- Java 11 (Utilizado para não quebrar instalação de apps pessoais com a atualização do Java)
- Spring Boot
- Kafka + Kafka UI
- PostgreSQL
- Docker + Docker Compose
- WireMock (API de fraudes mockada)
- Prometheus + Grafana
- Testes com JUnit 5 e Mockito

---

## 🚀 Como Executar

Certifique-se de ter **Docker** e **Docker Compose** instalados.

```bash
docker-compose up --build
```

A aplicação estará acessível em `http://localhost:8082`

---

## 📌 Endpoints Principais

### Criar Solicitação

`POST /policy-requests`

Payload de exemplo:
```json
{
  "customer_id": "adc56d77-348c-4bf0-908f-22d402ee715c",
  "product_id": "1b2da7cc-b367-4196-8a78-9cfeec21f587",
  "category": "AUTO",
  "salesChannel": "MOBILE",
  "paymentMethod": "CREDIT_CARD",
  "total_monthly_premium_amount": 75.25,
  "insured_amount": 275000.50,
  "coverages": {
    "Roubo": 100000.25,
    "Perda Total": 100000.25,
    "Colisão com Terceiros": 75000.00
  },
  "assistances": ["Guincho até 250km", "Troca de Óleo", "Chaveiro 24h"]
}
```

### Buscar por ID

`GET /policy-requests/<UUID>`

### Cancelar Solicitação

`DELETE /policy-requests/<UUID>`

### Buscar por Cliente

`GET /policy-requests?customerId=<UUID>`

---

## 🔁 Eventos Kafka

### Consumidores:
- `fraud-check` → classifica o cliente
- `payment-confirmed` → confirma pagamento

Exemplo evento esperado:
```json
{
   "requestId": "ce778f66-24c1-4ae7-b985-685335984e3a"
}
```

- `subscription-authorized` → autoriza subscrição

Exemplo evento esperado:
```json
{
   "requestId": "ce778f66-24c1-4ae7-b985-685335984e3a"
}
```

### Produtor:
- `policy-events` → publica transições de estado (ex: VALIDATED, APPROVED)

---

## 📊 Observabilidade

- **Kafka UI**: http://localhost:8080
- **Prometheus**: http://localhost:9090
- **Grafana**: http://localhost:3000 (usuário/senha padrão: `admin/admin`)

---

### 🔧 Como importar o Prometheus no Grafana (Não Funcional)

1. Acesse o Grafana em [http://localhost:3000](http://localhost:3000)
2. Vá até **"Gear" (⚙️) > Data Sources**
3. Clique em **"Add data source"**
4. Escolha a opção **Prometheus**
5. Em "URL", insira: `http://prometheus:9090`
6. Clique em **"Save & Test"**

---

## 🧪 Estratégia de Testes

- Testes unitários e de integração cobrindo:
  - Casos de uso
  - Serviços de aplicação
  - Validação de risco
  - Controllers
  - Kafka consumers e publishers
- Testes localizados em `src/test`


---
### Como executar:
	
```bash
./gradlew clean test jacocoTestReport
```
##Localização do relatório gerado
Após o build, abra o HTML de cobertura em:

```bash
build/reports/jacoco/jacocoHtml/index.html
```

---

## 🔍 Premissas e Decisões

- API de Fraudes é mockada com WireMock (`mock-fraud-api`)
- IDs de clientes e produtos são simulados com UUID
- Eventos são publicados mesmo em caso de falha para rastreamento
- Campos e regras de negócio foram seguidos fielmente ao PDF do desafio

