package com.acme.insurance.application.usecase;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CreatePolicyRequestDTO {

    @JsonProperty("customer_id")
    private UUID customerId;

    @JsonProperty("product_id")
    private UUID productId;
    @JsonProperty("category")
    private String category;
    @JsonProperty("salesChannel")
    private String salesChannel;
    @JsonProperty("paymentMethod")
    private String paymentMethod;

    @JsonProperty("total_monthly_premium_amount")
    private BigDecimal totalMonthlyPremiumAmount;

    @JsonProperty("insured_amount")
    private BigDecimal insuredAmount;
    @JsonProperty("coverages")
    private Map<String, BigDecimal> coverages;
    @JsonProperty("assistances")
    private List<String> assistances;
    @JsonProperty("createdAt")
    private Instant createdAt;
    @JsonProperty("finishedAt")
    private Instant finishedAt;

    // Getters and Setters

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSalesChannel() {
        return salesChannel;
    }

    public void setSalesChannel(String salesChannel) {
        this.salesChannel = salesChannel;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public BigDecimal getTotalMonthlyPremiumAmount() {
        return totalMonthlyPremiumAmount;
    }

    public void setTotalMonthlyPremiumAmount(BigDecimal totalMonthlyPremiumAmount) {
        this.totalMonthlyPremiumAmount = totalMonthlyPremiumAmount;
    }

    public BigDecimal getInsuredAmount() {
        return insuredAmount;
    }

    public void setInsuredAmount(BigDecimal insuredAmount) {
        this.insuredAmount = insuredAmount;
    }

    public Map<String, BigDecimal> getCoverages() {
        return coverages;
    }

    public void setCoverages(Map<String, BigDecimal> coverages) {
        this.coverages = coverages;
    }

    public List<String> getAssistances() {
        return assistances;
    }

    public void setAssistances(List<String> assistances) {
        this.assistances = assistances;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(Instant finishedAt) {
        this.finishedAt = finishedAt;
    }
}
