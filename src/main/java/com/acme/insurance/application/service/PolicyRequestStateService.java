package com.acme.insurance.application.service;

import com.acme.insurance.domain.model.PolicyRequest;
import com.acme.insurance.domain.model.PolicyRequestTransitionHelper;
import com.acme.insurance.domain.model.RiskClassification;
import com.acme.insurance.domain.model.StatusHistoryEntry;
import com.acme.insurance.domain.service.RiskValidationService;
import com.acme.insurance.infrastructure.repository.PolicyRequestRepository;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PolicyRequestStateService {

    private final PolicyRequestRepository repository;
    private final PolicyRequestEventPublisher eventPublisher;

    public PolicyRequestStateService(PolicyRequestRepository repository, PolicyRequestEventPublisher eventPublisher) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public void registerPayment(UUID requestId) {
        PolicyRequest request = repository.findById(requestId).orElseThrow();
        request.setPaymentConfirmed(true);
        updateStatusIfReady(request);
        repository.save(request);
        eventPublisher.publishByStatus(request);
    }

    @Transactional
    public void registerSubscription(UUID requestId) {
        PolicyRequest request = repository.findById(requestId).orElseThrow();
        request.setSubscriptionAuthorized(true);
        updateStatusIfReady(request);
        repository.save(request);
        eventPublisher.publishByStatus(request);
    }

    @Transactional
    public void registerPolicy(PolicyRequest request) {
        PolicyRequestTransitionHelper.markAsReceived(request);
        repository.save(request);
        eventPublisher.publishByStatus(request);
    }

    @Transactional
    public void rejectDueToPayment(UUID requestId) {
        PolicyRequest request = repository.findById(requestId).orElseThrow();
        PolicyRequestTransitionHelper.markAsRejected(request);
        repository.save(request);
        eventPublisher.publishByStatus(request);
    }

    @Transactional
    public void cancelPolicy(UUID requestId) {
        PolicyRequest request = repository.findById(requestId).orElseThrow();
        PolicyRequestTransitionHelper.markAsCancelled(request);
        repository.save(request);
        eventPublisher.publishByStatus(request);
    }

    @Transactional
    public void processFraudValidation(RiskClassification classification, UUID requestId) {
        PolicyRequest request = repository.findById(requestId).orElseThrow();

        boolean approved = RiskValidationService.validate(classification, request);

        if (approved) {
            PolicyRequestTransitionHelper.markAsValidated(request);
            PolicyRequestTransitionHelper.markAsPending(request);
        } else {
            PolicyRequestTransitionHelper.markAsRejected(request);
        }

        repository.save(request);
    }





    private void updateStatusIfReady(PolicyRequest request) {
        if (request.isPaymentConfirmed() && request.isSubscriptionAuthorized()) {
            PolicyRequestTransitionHelper.markAsApproved(request);
        }
    }
}