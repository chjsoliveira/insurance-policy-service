package com.acme.insurance.application.usecase;

import com.acme.insurance.domain.model.FraudOccurrence;
import com.acme.insurance.domain.model.RiskClassification;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class FraudCheckResponseDTO {
    private UUID orderId;
    private UUID customerId;
    private Instant analyzedAt;
    private RiskClassification classification;
    private List<FraudOccurrence> occurrences;

    // Getters e setters
    public UUID getOrderId() { return orderId; }
    public void setOrderId(UUID orderId) { this.orderId = orderId; }

    public UUID getCustomerId() { return customerId; }
    public void setCustomerId(UUID customerId) { this.customerId = customerId; }

    public Instant getAnalyzedAt() { return analyzedAt; }
    public void setAnalyzedAt(Instant analyzedAt) { this.analyzedAt = analyzedAt; }

    public RiskClassification getClassification() { return classification; }
    public void setClassification(RiskClassification classification) { this.classification = classification; }

    public List<FraudOccurrence> getOccurrences() { return occurrences; }
    public void setOccurrences(List<FraudOccurrence> occurrences) { this.occurrences = occurrences; }
}
