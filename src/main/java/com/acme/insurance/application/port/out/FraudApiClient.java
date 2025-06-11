package com.acme.insurance.application.port.out;

import com.acme.insurance.application.usecase.FraudCheckResponseDTO;

import java.util.UUID;

public interface FraudApiClient {
    FraudCheckResponseDTO analyze(UUID requestId);
}
