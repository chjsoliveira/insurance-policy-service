package com.acme.insurance.application.usecase;

import com.acme.insurance.application.service.PolicyRequestStateService;
import com.acme.insurance.domain.model.PolicyRequest;
import com.acme.insurance.domain.model.Status;
import com.acme.insurance.domain.model.StatusHistoryEntry;
import com.acme.insurance.infrastructure.repository.PolicyRequestRepository;
import com.acme.insurance.messaging.producer.KafkaProducerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class CancelPolicyRequestUseCase {
    private final PolicyRequestRepository repository;
    private final KafkaProducerService producer;
    private final ObjectMapper objectMapper;
    private final PolicyRequestStateService stateService;

    public CancelPolicyRequestUseCase(PolicyRequestRepository repository, KafkaProducerService producer, ObjectMapper objectMapper, PolicyRequestStateService stateService) {
        this.repository = repository;
        this.producer = producer;
        this.objectMapper = objectMapper;
        this.stateService = stateService;
    }

    public void execute(UUID id) throws JsonProcessingException {
        PolicyRequest request = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Policy request not found"));

        this.stateService.cancelPolicy(request.getId());
    }
}
