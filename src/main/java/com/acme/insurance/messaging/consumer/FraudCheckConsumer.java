package com.acme.insurance.messaging.consumer;

import com.acme.insurance.application.service.FraudProcessingService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class FraudCheckConsumer {
    private final FraudProcessingService fraudService;

    public FraudCheckConsumer(FraudProcessingService fraudService) {
        this.fraudService = fraudService;
    }

    @KafkaListener(topics = "policy-request.received", groupId = "insurance-policy-group")
    public void consume(String payload) {
        fraudService.processFraudAnalysis(payload);
    }
}