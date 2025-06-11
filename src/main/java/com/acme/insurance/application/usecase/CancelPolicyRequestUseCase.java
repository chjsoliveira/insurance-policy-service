package com.acme.insurance.application.usecase;

import com.acme.insurance.application.service.PolicyRequestStateService;
import com.acme.insurance.domain.model.PolicyRequest;
import com.acme.insurance.domain.model.Status;
import com.acme.insurance.domain.model.StatusHistoryEntry;
import com.acme.insurance.infrastructure.repository.PolicyRequestRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class CancelPolicyRequestUseCase {
    private final PolicyRequestRepository repository;
    private final PolicyRequestStateService stateService;

    public CancelPolicyRequestUseCase(PolicyRequestRepository repository, PolicyRequestStateService stateService) {
        this.repository = repository;
        this.stateService = stateService;
    }

    public void execute(UUID requestId) {
        PolicyRequest request = repository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Solicitação não encontrada: " + requestId));
        this.stateService.cancelPolicy(request.getId());
    }
}
