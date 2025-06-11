package com.acme.insurance.infrastructure.kafka.consumer;

import com.acme.insurance.application.service.FraudProcessingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

@Component
public class FraudCheckConsumer {
    private final FraudProcessingService fraudService;
    private static final Logger logger = LoggerFactory.getLogger(PaymentEventConsumer.class);

    public FraudCheckConsumer(FraudProcessingService fraudService) {
        this.fraudService = fraudService;
    }

    @KafkaListener(topics = "policy-request.received", groupId = "insurance-policy-group")
    public void consume(String payload) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode json = objectMapper.readTree(payload);
            UUID requestId = UUID.fromString(json.get("requestId").asText());
            logger.info("Solicitação de análse de Fraude recebida para requestId: {}", requestId);
            fraudService.processFraudAnalysis(requestId);
        } catch (IllegalArgumentException e) {
            logger.warn("Payload inválido, descartando evento: {}", payload, e);
        } catch (JsonProcessingException e) {
            logger.error("Erro de parsing JSON: {}", payload, e);
            throw new RuntimeException(e);
        } catch (Exception e) {
            logger.error("Erro inesperado no processamento, enviando para DLQ: {}", payload, e);
            throw e; 
        }
    }
}