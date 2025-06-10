package com.acme.insurance.application.usecase;

import com.acme.insurance.domain.model.PolicyRequest;
import com.acme.insurance.infrastructure.repository.PolicyRequestRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class FindPolicyRequestsByCustomerUseCase {
    private final PolicyRequestRepository repository;

    public FindPolicyRequestsByCustomerUseCase(PolicyRequestRepository repository) {
        this.repository = repository;
    }

    public List<PolicyRequest> execute(UUID customerId) {
        return repository.findByCustomerId(customerId);
    }
}
