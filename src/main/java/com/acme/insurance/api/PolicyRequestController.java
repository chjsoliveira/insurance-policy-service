package com.acme.insurance.api;

import com.acme.insurance.domain.model.PolicyRequest;
import com.acme.insurance.application.usecase.CreatePolicyRequestUseCase;
import com.acme.insurance.application.usecase.CreatePolicyRequestDTO;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/policy-requests")
public class PolicyRequestController {
    private final CreatePolicyRequestUseCase createUseCase;

    public PolicyRequestController(CreatePolicyRequestUseCase createUseCase) {
        this.createUseCase = createUseCase;
    }

    @PostMapping
    public ResponseEntity<PolicyRequest> create(@RequestBody CreatePolicyRequestDTO dto) throws JsonProcessingException {
        PolicyRequest result = createUseCase.execute(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
}
