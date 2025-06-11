package com.acme.insurance.application.usecase;

import com.acme.insurance.domain.model.PolicyRequest;
import com.acme.insurance.infrastructure.repository.PolicyRequestRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class FindPolicyRequestByIdUseCase {
    private final PolicyRequestRepository repository;

    public FindPolicyRequestByIdUseCase(PolicyRequestRepository repository) {
        this.repository = repository;
    }

    public Optional<PolicyRequest> execute(UUID requestId) {
        return repository.findById(requestId);
    }
}
