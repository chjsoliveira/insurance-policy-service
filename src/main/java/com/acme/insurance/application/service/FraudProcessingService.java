package com.acme.insurance.application.service;

import com.acme.insurance.application.usecase.PolicyEventPayload;
import com.acme.insurance.domain.model.*;
import com.acme.insurance.domain.service.RiskValidationService;
import com.acme.insurance.infrastructure.repository.PolicyRequestRepository;
import com.acme.insurance.messaging.producer.KafkaProducerService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

import java.util.UUID;

@Service
public class FraudProcessingService {
    private final PolicyRequestRepository repository;
    private final KafkaProducerService producer;
    private final FraudApiClientImpl fraudApiClient;
    private final PolicyRequestStateService stateService;

    public FraudProcessingService(PolicyRequestRepository repository, KafkaProducerService producer,
                                  FraudApiClientImpl fraudApiClient, PolicyRequestStateService stateService) {
        this.repository = repository;
        this.producer = producer;
        this.fraudApiClient = fraudApiClient;
        this.stateService = stateService;
    }

    public void processFraudAnalysis(String payload) {
        // Simulação de parsing simplificado
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode json = mapper.readTree(payload);
            UUID requestId = UUID.fromString(json.get("orderId").asText());
            PolicyRequest request = repository.findById(requestId).orElseThrow();
            // Simula chamada externa à API de fraudes (mockada)
            RiskClassification classification = fraudApiClient.analyze(request.getId()).getClassification();

            boolean approved = RiskValidationService.validate(classification, request);
            stateService.processFraudValidation(approved, requestId);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
