
package com.acme.insurance.application.service;

import com.acme.insurance.application.usecase.FraudCheckResponseDTO;
import com.acme.insurance.domain.model.*;
import com.acme.insurance.infrastructure.repository.PolicyRequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

public class FraudProcessingServiceTest {

    @Mock
    private PolicyRequestRepository repository;

    @Mock
    private FraudApiClientImpl fraudApiClient;

    @Mock
    private PolicyRequestStateService stateService;

    @InjectMocks
    private FraudProcessingService fraudProcessingService;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldProcessRegularClientWithValidAmount() {
        UUID requestId = UUID.randomUUID();
        PolicyRequest request = new PolicyRequest();
        request.setRequestId(requestId);
        request.setCategory(Category.valueOf("AUTO"));
        request.setInsuredAmount(BigDecimal.valueOf(300000.00));

        when(repository.findById(requestId)).thenReturn(Optional.of(request));

        FraudCheckResponseDTO dto = new FraudCheckResponseDTO();
        dto.setOrderId(requestId);
        dto.setCustomerId(request.getCustomerId());
        dto.setAnalyzedAt(Instant.parse("2024-05-10T12:00:00Z"));
        dto.setClassification(RiskClassification.HIGH_RISK);
        when(fraudApiClient.analyze(any())).thenReturn(dto);
        fraudProcessingService.processFraudAnalysis(requestId);

        verify(stateService).processFraudValidation(eq(RiskClassification.HIGH_RISK), eq(request.getRequestId()));
    }

}
