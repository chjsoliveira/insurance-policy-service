
package com.acme.insurance.application.service;

import com.acme.insurance.domain.model.Category;
import com.acme.insurance.domain.model.PolicyRequest;
import com.acme.insurance.domain.model.RiskClassification;
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
        request.setRequestId(requestId);
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
        request.setRequestId(requestId);
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
    @Test
    void shouldRejectDueToPaymentWhenPending() {
        UUID id = UUID.randomUUID();
        PolicyRequest request = new PolicyRequest();
        request.setRequestId(id);
        request.setStatus(Status.PENDING);

        when(repository.findById(id)).thenReturn(Optional.of(request));
        when(repository.save(any())).thenReturn(request);

        stateService.rejectDueToPayment(id);

        assertFalse(request.isPaymentConfirmed());
        assertEquals(Status.REJECTED, request.getStatus());
        verify(repository).save(request);
    }

    @Test
    void shouldCancelWhenNotApproved() {
        UUID id = UUID.randomUUID();
        PolicyRequest request = new PolicyRequest();
        request.setRequestId(id);
        request.setStatus(Status.VALIDATED);

        when(repository.findById(id)).thenReturn(Optional.of(request));
        when(repository.save(any())).thenReturn(request);

        stateService.cancelPolicy(id);
        assertEquals(Status.CANCELLED, request.getStatus());
        verify(repository).save(request);
    }

    @Test
    void shouldNotCancelWhenApproved() {
        UUID id = UUID.randomUUID();
        PolicyRequest request = new PolicyRequest();
        request.setRequestId(id);
        request.setStatus(Status.APPROVED);

        when(repository.findById(id)).thenReturn(Optional.of(request));

        assertThrows(IllegalStateException.class, () -> stateService.cancelPolicy(id));

        assertEquals(Status.APPROVED, request.getStatus());

        verify(repository, never()).save(any());
    }

    @Test
    void shouldRegisterPolicyWhenPending() {
        UUID id = UUID.randomUUID();
        PolicyRequest request = new PolicyRequest();
        request.setRequestId(id);
        request.setInsuredAmount(new BigDecimal("500000"));
        request.setCategory(Category.AUTO);

        when(repository.findById(id)).thenReturn(Optional.of(request));
        when(repository.save(any())).thenReturn(request);

        stateService.registerPolicy(request);

        assertEquals(Status.RECEIVED, request.getStatus());
        assertNull(request.getFinishedAt());
        verify(repository).save(request);
    }
    @Test
    void shouldProcessValidationAndMarkAsPendingIfApproved() {
        PolicyRequest request = new PolicyRequest();
        request.setRequestId(UUID.randomUUID());
        request.setStatus(Status.RECEIVED);
        request.setInsuredAmount(new BigDecimal("300000"));
        request.setCategory(Category.AUTO);

        stateService.processFraudValidation(RiskClassification.REGULAR, request);

        assertEquals(Status.PENDING, request.getStatus());
        assertTrue(request.getHistory().stream().anyMatch(h -> h.getStatus() == Status.VALIDATED));
        assertTrue(request.getHistory().stream().anyMatch(h -> h.getStatus() == Status.PENDING));
    }

    @Test
    void shouldProcessValidationAndMarkAsRejectedIfNotApproved() {
        PolicyRequest request = new PolicyRequest();
        request.setRequestId(UUID.randomUUID());
        request.setStatus(Status.RECEIVED);
        request.setInsuredAmount(new BigDecimal("900000"));
        request.setCategory(Category.AUTO);

        stateService.processFraudValidation(RiskClassification.REGULAR, request);

        assertEquals(Status.REJECTED, request.getStatus());
        assertTrue(request.getHistory().stream().anyMatch(h -> h.getStatus() == Status.REJECTED));
    }
}
