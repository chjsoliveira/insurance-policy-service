package com.acme.insurance.infrastructure.kafka.consumer;

import com.acme.insurance.application.service.FraudProcessingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class FraudCheckConsumerTest {

    private FraudProcessingService fraudService;
    private FraudCheckConsumer consumer;

    @BeforeEach
    void setup() {
        fraudService = mock(FraudProcessingService.class);
        consumer = new FraudCheckConsumer(fraudService);
    }

    @Test
    void shouldProcessValidPayload() {
        UUID requestId = UUID.randomUUID();
        String payload = "{ \"requestId\": \"" + requestId.toString() + "\" }";

        consumer.consume(payload);

        verify(fraudService).processFraudAnalysis(requestId);
    }

    @Test
    void shouldLogWarningAndIgnoreInvalidUUID() {
        String payload = "{ \"requestId\": \"not-a-uuid\" }";

        consumer.consume(payload);

        verifyNoInteractions(fraudService);
    }

    @Test
    void shouldThrowExceptionOnInvalidJson() {
        String payload = "{ invalid_json }";

        assertThrows(RuntimeException.class, () -> consumer.consume(payload));
        verifyNoInteractions(fraudService);
    }

    @Test
    void shouldRethrowUnexpectedException() {
        UUID requestId = UUID.randomUUID();
        String payload = "{ \"requestId\": \"" + requestId.toString() + "\" }";

        doThrow(new RuntimeException("boom")).when(fraudService).processFraudAnalysis(requestId);

        assertThrows(RuntimeException.class, () -> consumer.consume(payload));
        verify(fraudService).processFraudAnalysis(requestId);
    }
}
