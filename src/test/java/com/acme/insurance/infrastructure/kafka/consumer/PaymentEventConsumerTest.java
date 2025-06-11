package com.acme.insurance.infrastructure.kafka.consumer;

import com.acme.insurance.application.service.PolicyRequestStateService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class PaymentEventConsumerTest {

    private PolicyRequestStateService stateService;
    private ObjectMapper objectMapper;
    private PaymentEventConsumer consumer;

    @BeforeEach
    void setup() {
        stateService = mock(PolicyRequestStateService.class);
        objectMapper = new ObjectMapper();
        consumer = new PaymentEventConsumer(stateService, objectMapper);
    }

    @Test
    void shouldHandlePaymentConfirmed() {
        UUID requestId = UUID.randomUUID();
        String payload = "{ \"requestId\": \"" + requestId.toString() + "\" }";

        consumer.handlePaymentConfirmed(payload);

        verify(stateService).registerPayment(requestId);
    }

    @Test
    void shouldHandlePaymentDenied() {
        UUID requestId = UUID.randomUUID();
        String payload = "{ \"requestId\": \"" + requestId.toString() + "\" }";

        consumer.handlePaymentDenied(payload);

        verify(stateService).rejectDueToPayment(requestId);
    }

    @Test
    void shouldThrowExceptionOnInvalidJsonConfirmed() {
        String payload = "{ not_json }";

        assertThrows(RuntimeException.class, () -> consumer.handlePaymentConfirmed(payload));
        verifyNoInteractions(stateService);
    }

    @Test
    void shouldThrowExceptionOnInvalidJsonDenied() {
        String payload = "{ not_json }";

        assertThrows(RuntimeException.class, () -> consumer.handlePaymentDenied(payload));
        verifyNoInteractions(stateService);
    }
}
