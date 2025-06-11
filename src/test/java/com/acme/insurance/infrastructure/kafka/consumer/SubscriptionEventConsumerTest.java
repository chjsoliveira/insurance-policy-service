package com.acme.insurance.infrastructure.kafka.consumer;

import com.acme.insurance.application.service.PolicyRequestStateService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.mockito.Mockito.*;

public class SubscriptionEventConsumerTest {

    private PolicyRequestStateService stateService;
    private ObjectMapper objectMapper;
    private SubscriptionEventConsumer consumer;

    @BeforeEach
    void setup() {
        stateService = mock(PolicyRequestStateService.class);
        objectMapper = new ObjectMapper();
        consumer = new SubscriptionEventConsumer(stateService, objectMapper);
    }

    @Test
    void shouldHandleSubscriptionApproved() {
        UUID requestId = UUID.randomUUID();
        String payload = "{ \"requestId\": \"" + requestId.toString() + "\" }";

        consumer.handleSubscriptionApproved(payload);

        verify(stateService).registerSubscription(requestId);
    }

    @Test
    void shouldIgnoreInvalidJson() {
        String payload = "{ invalid_json }";

        // Deve capturar o erro silenciosamente (sem lançar exceção)
        consumer.handleSubscriptionApproved(payload);

        verifyNoInteractions(stateService);
    }
}
