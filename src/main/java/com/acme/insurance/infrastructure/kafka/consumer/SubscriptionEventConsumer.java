package com.acme.insurance.infrastructure.kafka.consumer;

import com.acme.insurance.application.service.PolicyRequestStateService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class SubscriptionEventConsumer {

    private static final Logger logger = LoggerFactory.getLogger(SubscriptionEventConsumer.class);
    private final PolicyRequestStateService stateService;
    private final ObjectMapper objectMapper;

    public SubscriptionEventConsumer(PolicyRequestStateService stateService, ObjectMapper objectMapper) {
        this.stateService = stateService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "subscription.approved", groupId = "insurance-policy-group")
    public void handleSubscriptionApproved(String payload) {
        try {
            JsonNode json = objectMapper.readTree(payload);
            UUID requestId = UUID.fromString(json.get("requestId").asText());
            logger.info("Subscrição aprovada recebida para requestId: {}", requestId);
            stateService.registerSubscription(requestId);
        } catch (Exception e) {
            logger.error("Erro ao processar evento de subscrição aprovada: {}", payload, e);
        }
    }
}
