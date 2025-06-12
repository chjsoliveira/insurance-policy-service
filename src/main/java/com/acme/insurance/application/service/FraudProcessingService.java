package com.acme.insurance.application.service;

import com.acme.insurance.domain.model.*;
import com.acme.insurance.domain.service.RiskValidationService;
import com.acme.insurance.infrastructure.repository.PolicyRequestRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.UUID;

@Service
public class FraudProcessingService {
    private final PolicyRequestRepository repository;
    private final FraudApiClientImpl fraudApiClient;
    private final PolicyRequestStateService stateService;
    private static final Logger logger = LoggerFactory.getLogger(FraudProcessingService.class);

    public FraudProcessingService(PolicyRequestRepository repository,
                                  FraudApiClientImpl fraudApiClient, PolicyRequestStateService stateService) {
        this.repository = repository;

        this.fraudApiClient = fraudApiClient;
        this.stateService = stateService;
    }
    public void processFraudAnalysis(UUID requestId) {
        try {
            MDC.put("requestId", requestId.toString());
            logger.info("Iniciando processamento de fraude para apólice {}", requestId);
            //PolicyRequest request = repository.findById(requestId).orElseThrow();
            RiskClassification classification = fraudApiClient.analyze(requestId).getClassification();
            stateService.processFraudValidation(classification, requestId);
            logger.info("Processamento de fraude concluído. Classificação: {}", classification);
        } catch (IllegalArgumentException e) {
            logger.warn("Erro de negócio, não será reprocessado: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Erro inesperado ao processar evento. Enviar para DLQ", e);
            throw e;
        } finally {
            MDC.clear();
        }
    }
}
