
package com.acme.insurance.application.service;

import com.acme.insurance.domain.model.Category;
import com.acme.insurance.domain.model.PolicyRequest;
import com.acme.insurance.domain.model.Status;
import com.acme.insurance.infrastructure.repository.PolicyRequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PolicyRequestStateServiceTest {

    @Mock
    private PolicyRequestRepository repository;

    @Mock
    private PolicyRequestEventPublisher eventPublisher;

    @InjectMocks
    private PolicyRequestStateService stateService;

    private AutoCloseable closeable;

    @BeforeEach
    void init() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldRegisterPaymentAndPublishEvent() {
        UUID requestId = UUID.randomUUID();
        PolicyRequest request = new PolicyRequest();
        request.setId(requestId);
        request.setSubscriptionAuthorized(true);
        request.setStatus(Status.PENDING); // ou RECEIVED, conforme seu fluxo
        request.setSubscriptionAuthorized(true); // ou false, dependendo do cenário
        request.setCategory(Category.AUTO);
        request.setInsuredAmount(new BigDecimal("200000"));
        request.setHistory(new ArrayList<>());

        when(repository.findById(requestId)).thenReturn(Optional.of(request));

        stateService.registerPayment(requestId);

        assertTrue(request.isPaymentConfirmed());
        verify(repository).save(request);
        verify(eventPublisher).publishByStatus(request);
    }

    @Test
    void shouldRegisterSubscriptionAndPublishEvent() {
        UUID requestId = UUID.randomUUID();
        PolicyRequest request = new PolicyRequest();
        request.setId(requestId);
        request.setPaymentConfirmed(true);
        request.setStatus(Status.PENDING); // ou RECEIVED, conforme seu fluxo
        request.setSubscriptionAuthorized(true); // ou false, dependendo do cenário
        request.setCategory(Category.AUTO);
        request.setInsuredAmount(new BigDecimal("200000"));
        request.setHistory(new ArrayList<>());

        when(repository.findById(requestId)).thenReturn(Optional.of(request));

        stateService.registerSubscription(requestId);

        assertTrue(request.isSubscriptionAuthorized());
        verify(repository).save(request);
        verify(eventPublisher).publishByStatus(request);
    }

    @Test
    void shouldThrowWhenRequestNotFound() {
        UUID requestId = UUID.randomUUID();
        when(repository.findById(requestId)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> stateService.registerPayment(requestId));
    }
}
