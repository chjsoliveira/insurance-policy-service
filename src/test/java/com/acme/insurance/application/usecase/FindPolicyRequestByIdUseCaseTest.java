package com.acme.insurance.application.usecase;

import com.acme.insurance.domain.model.PolicyRequest;
import com.acme.insurance.infrastructure.repository.PolicyRequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FindPolicyRequestByIdUseCaseTest {

    private PolicyRequestRepository repository;
    private FindPolicyRequestByIdUseCase useCase;

    @BeforeEach
    void setUp() {
        repository = mock(PolicyRequestRepository.class);
        useCase = new FindPolicyRequestByIdUseCase(repository);
    }

    @Test
    void shouldReturnPolicyRequestById() {
        UUID requestId = UUID.randomUUID();
        PolicyRequest expected = new PolicyRequest();
        expected.setRequestId(requestId);

        when(repository.findById(requestId)).thenReturn(Optional.of(expected));

        Optional<PolicyRequest> result = useCase.execute(requestId);

        assertTrue(result.isPresent());
        assertEquals(requestId, result.get().getRequestId());
        verify(repository).findById(requestId);
    }

    @Test
    void shouldReturnEmptyWhenPolicyRequestNotFound() {
        UUID requestId = UUID.randomUUID();

        when(repository.findById(requestId)).thenReturn(Optional.empty());

        Optional<PolicyRequest> result = useCase.execute(requestId);

        assertTrue(result.isEmpty());
        verify(repository).findById(requestId);
    }
}
