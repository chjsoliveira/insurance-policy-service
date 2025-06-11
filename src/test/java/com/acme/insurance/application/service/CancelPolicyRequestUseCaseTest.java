
package com.acme.insurance.application.usecase;

import com.acme.insurance.application.service.PolicyRequestStateService;
import com.acme.insurance.domain.model.PolicyRequest;
import com.acme.insurance.infrastructure.repository.PolicyRequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class CancelPolicyRequestUseCaseTest {

    @Mock
    private PolicyRequestRepository repository;

    @Mock
    private PolicyRequestStateService stateService;

    @InjectMocks
    private CancelPolicyRequestUseCase cancelUseCase;

    private AutoCloseable closeable;

    @BeforeEach
    void init() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCancelPolicyIfRequestExists() {
        UUID requestId = UUID.randomUUID();
        PolicyRequest request = new PolicyRequest();
        request.setId(requestId);

        when(repository.findById(requestId)).thenReturn(Optional.of(request));

        cancelUseCase.execute(requestId);

        verify(stateService).cancelPolicy(requestId);
    }

    @Test
    void shouldThrowExceptionIfRequestNotFound() {
        UUID requestId = UUID.randomUUID();
        when(repository.findById(requestId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> cancelUseCase.execute(requestId));
    }
}
