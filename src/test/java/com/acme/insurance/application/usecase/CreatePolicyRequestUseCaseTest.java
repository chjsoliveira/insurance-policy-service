package com.acme.insurance.application.usecase;

import com.acme.insurance.domain.model.Category;
import com.acme.insurance.domain.model.PolicyRequest;
import com.acme.insurance.application.service.PolicyRequestStateService;
import com.acme.insurance.infrastructure.repository.PolicyRequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CreatePolicyRequestUseCaseTest {

    private PolicyRequestRepository repository;
    private PolicyRequestStateService stateService;
    private CreatePolicyRequestUseCase useCase;

    @BeforeEach
    void setup() {
        repository = mock(PolicyRequestRepository.class);
        stateService = mock(PolicyRequestStateService.class);
        useCase = new CreatePolicyRequestUseCase(repository, stateService);
    }

    @Test
    void shouldCreateAndRegisterPolicyRequest() {
        CreatePolicyRequestDTO dto = new CreatePolicyRequestDTO();
        dto.setCustomerId(UUID.randomUUID());
        dto.setProductId(UUID.randomUUID());
        dto.setCategory("AUTO");
        dto.setSalesChannel("MOBILE");
        dto.setPaymentMethod("CREDIT_CARD");
        dto.setTotalMonthlyPremiumAmount(new BigDecimal("75.25"));
        dto.setInsuredAmount(new BigDecimal("275000.50"));
        dto.setCoverages(Map.of("Roubo", new BigDecimal("100000.25")));
        dto.setAssistances(java.util.List.of("Guincho até 250km", "Troca de Óleo"));

        PolicyRequest saved = new PolicyRequest();
        saved.setRequestId(UUID.randomUUID());

        when(repository.save(any())).thenReturn(saved);

        PolicyRequest result = useCase.execute(dto);

        assertNotNull(result);
        assertNotNull(result.getRequestId());
        assertEquals(dto.getCustomerId(), result.getCustomerId());
        assertEquals(Category.AUTO, result.getCategory());
        assertEquals(dto.getInsuredAmount(), result.getInsuredAmount());

        verify(repository).save(any(PolicyRequest.class));
        verify(stateService).registerPolicy(any(PolicyRequest.class));
    }
}
