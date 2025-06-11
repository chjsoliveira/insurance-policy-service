package com.acme.insurance.application.usecase;

import java.time.LocalDateTime;
import java.util.UUID;

import com.acme.insurance.application.service.PolicyRequestStateService;
import com.acme.insurance.domain.model.*;
import com.acme.insurance.infrastructure.repository.PolicyRequestRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class CreatePolicyRequestUseCase {
    private final PolicyRequestRepository repository;
    private final PolicyRequestStateService stateService;

    public CreatePolicyRequestUseCase(PolicyRequestRepository repository, PolicyRequestStateService stateService) {
        this.repository = repository;
        this.stateService = stateService;
    }

    public PolicyRequest execute(CreatePolicyRequestDTO dto) {
        PolicyRequest request = new PolicyRequest();
        request.setId(UUID.randomUUID());
        request.setCustomerId(dto.getCustomerId());
        request.setProductId(dto.getProductId());
        request.setCategory(Category.valueOf(dto.getCategory().toUpperCase()));
        request.setSalesChannel(dto.getSalesChannel());
        request.setPaymentMethod(dto.getPaymentMethod());
        request.setTotalMonthlyPremiumAmount(dto.getTotalMonthlyPremiumAmount());
        request.setInsuredAmount(dto.getInsuredAmount());
        request.setCoverages(dto.getCoverages());
        request.setAssistances(dto.getAssistances());
        repository.save(request);
        this.stateService.registerPolicy(request);
        return request;
    }
	
}
