package com.acme.insurance.api;

import com.acme.insurance.domain.model.PolicyRequest;
import com.acme.insurance.application.usecase.CancelPolicyRequestUseCase;
import com.acme.insurance.application.usecase.FindPolicyRequestByIdUseCase;
import com.acme.insurance.application.usecase.FindPolicyRequestsByCustomerUseCase;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/policy-requests")
public class PolicyRequestQueryController {
    private final FindPolicyRequestByIdUseCase findByIdUseCase;
    private final FindPolicyRequestsByCustomerUseCase findByCustomerUseCase;
    private final CancelPolicyRequestUseCase cancelUseCase;

    public PolicyRequestQueryController(
        FindPolicyRequestByIdUseCase findByIdUseCase,
        FindPolicyRequestsByCustomerUseCase findByCustomerUseCase,
        CancelPolicyRequestUseCase cancelUseCase
    ) {
        this.findByIdUseCase = findByIdUseCase;
        this.findByCustomerUseCase = findByCustomerUseCase;
        this.cancelUseCase = cancelUseCase;
    }

    @GetMapping("/{id}")
    public ResponseEntity<PolicyRequest> getById(@PathVariable UUID id) {
        return findByIdUseCase.execute(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<PolicyRequest>> getByCustomerId(@RequestParam UUID customerId) {
        return ResponseEntity.ok(findByCustomerUseCase.execute(customerId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancel(@PathVariable UUID id) {
        try {
            cancelUseCase.execute(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

}
