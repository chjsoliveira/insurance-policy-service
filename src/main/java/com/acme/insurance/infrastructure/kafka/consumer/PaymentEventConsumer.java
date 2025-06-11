package com.acme.insurance.infrastructure.kafka.consumer;

import com.acme.insurance.application.service.PolicyRequestStateService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class PaymentEventConsumer {

    private static final Logger logger = LoggerFactory.getLogger(PaymentEventConsumer.class);

    private final PolicyRequestStateService stateService;
    private final ObjectMapper objectMapper;

    public PaymentEventConsumer(
            PolicyRequestStateService stateService,
            ObjectMapper objectMapper
    ) {
        this.stateService = stateService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "payment.confirmed", groupId = "insurance-policy-group")
    public void handlePaymentConfirmed(String payload) {
        try {
            JsonNode json = objectMapper.readTree(payload);
            UUID requestId = UUID.fromString(json.get("requestId").asText());
            logger.info("Pagamento confirmado recebido para requestId: {}", requestId);
            stateService.registerPayment(requestId);
        } catch (JsonProcessingException e) {
            logger.error("Erro de parsing JSON: {}", payload, e);
            throw new RuntimeException(e);
        } catch (Exception e) {
            logger.error("Erro ao processar evento de pagamento confirmado: {}", payload, e);
            throw e;
        }
    }

    @KafkaListener(topics = "payment.denied", groupId = "insurance-policy-group")
    public void handlePaymentDenied(String payload) {
        try {
            JsonNode json = objectMapper.readTree(payload);
            UUID requestId = UUID.fromString(json.get("requestId").asText());
            logger.info("Pagamento negado recebido para requestId: {}", requestId);
            stateService.rejectDueToPayment(requestId);
        } catch (JsonProcessingException e) {
            logger.error("Erro de parsing JSON: {}", payload, e);
            throw new RuntimeException(e);
        } catch (Exception e) {
            logger.error("Erro ao processar evento de pagamento negado: {}", payload, e);
            throw e;
        }
    }
}
