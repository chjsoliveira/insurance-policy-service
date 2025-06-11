package com.acme.insurance.domain.model;

import com.acme.insurance.application.usecase.CreatePolicyRequestDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Entity
public class PolicyRequest {

    public PolicyRequest() {
        this.history = new ArrayList<>();
    }
    @Id
    @Column(name = "id")
    @JsonProperty("id")
    private UUID requestId;

    @JsonProperty("customer_id")
    private UUID customerId;

    @JsonProperty("product_id")
    private UUID productId;

    @JsonProperty("category")
    private Category category;

    @JsonProperty("salesChannel")
    private String salesChannel;

    @JsonProperty("paymentMethod")
    private String paymentMethod;

    @JsonProperty("total_monthly_premium_amount")
    private BigDecimal totalMonthlyPremiumAmount;

    @JsonProperty("insured_amount")
    private BigDecimal insuredAmount;

    @ElementCollection
    @CollectionTable(name = "policy_request_coverages", joinColumns = @JoinColumn(name = "policy_request_id"))
    @MapKeyColumn(name = "coverage_name")
    @Column(name = "coverage_value")
    @JsonProperty("coverages")
    private Map<String, BigDecimal> coverages = new HashMap<>();

    @ElementCollection
    @CollectionTable(name = "policy_request_assistances", joinColumns = @JoinColumn(name = "policy_request_id"))
    @Column(name = "assistance")
    @JsonProperty("assistances")
    private List<String> assistances = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @JsonProperty("status")
    private Status status;

    @JsonProperty("createdAt")
    private LocalDateTime createdAt;

    @JsonProperty("finishedAt")
    private LocalDateTime finishedAt;

    @ElementCollection
    @CollectionTable(name = "policy_request_history", joinColumns = @JoinColumn(name = "policy_request_id"))
    @JsonProperty("history")
    private List<StatusHistoryEntry> history = new ArrayList<>();

    private boolean paymentConfirmed;
    private boolean subscriptionAuthorized;

    // Getters e setters

    public UUID getRequestId() { return requestId; }
    public void setRequestId(UUID requestId) { this.requestId = requestId; }

    public UUID getCustomerId() { return customerId; }
    public void setCustomerId(UUID customerId) { this.customerId = customerId; }

    public UUID getProductId() { return productId; }
    public void setProductId(UUID productId) { this.productId = productId; }

    @Enumerated(EnumType.STRING)
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public String getSalesChannel() { return salesChannel; }
    public void setSalesChannel(String salesChannel) { this.salesChannel = salesChannel; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public BigDecimal getTotalMonthlyPremiumAmount() { return totalMonthlyPremiumAmount; }
    public void setTotalMonthlyPremiumAmount(BigDecimal totalMonthlyPremiumAmount) {
        this.totalMonthlyPremiumAmount = totalMonthlyPremiumAmount;
    }

    public BigDecimal getInsuredAmount() { return insuredAmount; }
    public void setInsuredAmount(BigDecimal insuredAmount) { this.insuredAmount = insuredAmount; }

    public Map<String, BigDecimal> getCoverages() { return coverages; }
    public void setCoverages(Map<String, BigDecimal> coverages) { this.coverages = coverages; }

    public List<String> getAssistances() { return assistances; }
    public void setAssistances(List<String> assistances) { this.assistances = assistances; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getFinishedAt() { return finishedAt; }
    public void setFinishedAt(LocalDateTime finishedAt) { this.finishedAt = finishedAt; }

    public List<StatusHistoryEntry> getHistory() { return history; }
    public void setHistory(List<StatusHistoryEntry> history) { this.history = history; }

    public boolean isPaymentConfirmed() {
        return paymentConfirmed;
    }

    public void setPaymentConfirmed(boolean paymentConfirmed) {
        this.paymentConfirmed = paymentConfirmed;
    }

    public boolean isSubscriptionAuthorized() {
        return subscriptionAuthorized;
    }

    public void setSubscriptionAuthorized(boolean subscriptionAuthorized) {
        this.subscriptionAuthorized = subscriptionAuthorized;
    }

    public void updateStatus(Status newStatus) {
        this.status = newStatus;
        this.history.add(new StatusHistoryEntry(newStatus, LocalDateTime.now()));

        if (newStatus == Status.APPROVED || newStatus == Status.REJECTED || newStatus == Status.CANCELLED) {
            this.finishedAt = LocalDateTime.now();
        }
        if (newStatus == Status.RECEIVED)
        {
            this.createdAt = LocalDateTime.now();
        }
    }

    public String toEventPayload() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize event", e);
        }
    }
}
