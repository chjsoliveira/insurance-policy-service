#!/bin/sh

echo "Iniciando configuração do RabbitMQ..."

RABBITMQ_USER=admin
RABBITMQ_PASS=admin
RABBITMQ_API_URL=http://rabbitmq:15672/api
VHOST=%2f  # default vhost encoded
EXCHANGE="policy-events"
EXCHANGE_TYPE="topic"

# Define as filas e suas routing keys
QUEUES="fraud-analyzed payment-confirmed underwriting-approved policy-cancelled policy-issued policy-status-updated"
ROUTING_KEYS="fraud.analyzed payment.confirmed underwriting.approved policy.cancelled policy.issued policy.status.updated"

i=1
for queue in $QUEUES; do
  routing_key=$(echo $ROUTING_KEYS | cut -d' ' -f$i)

  echo "Criando fila: $queue"
  curl -s -u $RABBITMQ_USER:$RABBITMQ_PASS -X PUT -H "Content-Type: application/json" \
    -d '{"durable":true}' \
    "$RABBITMQ_API_URL/queues/$VHOST/$queue"

  i=$((i + 1))
done

echo "Criando exchange: $EXCHANGE"
curl -s -u $RABBITMQ_USER:$RABBITMQ_PASS -X PUT -H "Content-Type: application/json" \
  -d "{\"type\":\"$EXCHANGE_TYPE\",\"durable\":true}" \
  "$RABBITMQ_API_URL/exchanges/$VHOST/$EXCHANGE"

# Reset o contador
i=1
for queue in $QUEUES; do
  routing_key=$(echo $ROUTING_KEYS | cut -d' ' -f$i)

  echo "🔗 Binding exchange '$EXCHANGE' -> queue '$queue' [routing_key=$routing_key]"
  curl -s -u $RABBITMQ_USER:$RABBITMQ_PASS -X POST -H "Content-Type: application/json" \
    -d "{\"routing_key\":\"$routing_key\",\"destination_type\":\"queue\",\"destination\":\"$queue\"}" \
    "$RABBITMQ_API_URL/bindings/$VHOST/e/$EXCHANGE/q/$queue"

  i=$((i + 1))
done

echo "✅ Configuração concluída!"
