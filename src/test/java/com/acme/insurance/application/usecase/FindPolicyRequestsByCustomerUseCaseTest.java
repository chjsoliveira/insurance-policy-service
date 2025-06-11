package com.acme.insurance.application.usecase;

import com.acme.insurance.domain.model.PolicyRequest;
import com.acme.insurance.infrastructure.repository.PolicyRequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FindPolicyRequestsByCustomerUseCaseTest {

    private PolicyRequestRepository repository;
    private FindPolicyRequestsByCustomerUseCase useCase;

    @BeforeEach
    void setUp() {
        repository = mock(PolicyRequestRepository.class);
        useCase = new FindPolicyRequestsByCustomerUseCase(repository);
    }

    @Test
    void shouldReturnPolicyRequestsByCustomerId() {
        UUID customerId = UUID.randomUUID();
        PolicyRequest request = new PolicyRequest();
        request.setCustomerId(customerId);

        when(repository.findByCustomerId(customerId)).thenReturn(List.of(request));

        List<PolicyRequest> result = useCase.execute(customerId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(customerId, result.get(0).getCustomerId());

        verify(repository).findByCustomerId(customerId);
    }
}
