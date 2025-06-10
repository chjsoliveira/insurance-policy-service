package com.acme.insurance.application.service;

import com.acme.insurance.application.usecase.PolicyEventPayload;
import com.acme.insurance.domain.model.PolicyRequest;
import com.acme.insurance.domain.model.Status;
import com.acme.insurance.messaging.producer.KafkaProducerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class PolicyRequestEventPublisher {

    private final KafkaProducerService kafkaProducer;
    private final ObjectMapper objectMapper;

    public PolicyRequestEventPublisher(KafkaProducerService kafkaProducer, ObjectMapper objectMapper) {
        this.kafkaProducer = kafkaProducer;
        this.objectMapper = objectMapper;
    }

    public void publishByStatus(PolicyRequest request) {
        Status status = request.getStatus();
        String topic = "policy-request." + status.name().toLowerCase();
        send(topic, request);
    }

    public void publishStatusChanged(PolicyRequest request) {
        String topicSuffix = request.getStatus().name().toLowerCase();
        String topic = "policy-request." + topicSuffix;
        send(topic, request);
    }

    private void send(String topic, PolicyRequest request) {
        try {
            String json = objectMapper.writeValueAsString(toPayload(request));
            kafkaProducer.send(topic, json);
        } catch (Exception e) {
            // logar ou tratar erro
        }
    }

    private PolicyEventPayload toPayload(PolicyRequest request) {
        return new PolicyEventPayload(
                request.getId(),
                request.getCustomerId(),
                request.getStatus()
        );
    }
}
