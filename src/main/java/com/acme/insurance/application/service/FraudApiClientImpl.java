package com.acme.insurance.application.service;

import com.acme.insurance.application.port.out.FraudApiClient;
import com.acme.insurance.application.usecase.FraudCheckResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
public class FraudApiClientImpl implements FraudApiClient {

    private final RestTemplate restTemplate;

    public FraudApiClientImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public FraudCheckResponseDTO analyze(UUID requestId) {
        String url = "http://mock-fraud-api:8080/fraud-check/" + requestId;
        return restTemplate.getForObject(url, FraudCheckResponseDTO.class);
    }
}
