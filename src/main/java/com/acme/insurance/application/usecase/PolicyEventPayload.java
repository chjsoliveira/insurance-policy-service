package com.acme.insurance.application.usecase;

import com.acme.insurance.domain.model.Status;

import java.time.LocalDateTime;
import java.util.UUID;

public class PolicyEventPayload implements KafkaMessageKeyAware {
    private final UUID requestId;
    private final UUID customerId;
    private final Status status;
    private final LocalDateTime timestamp;

    public PolicyEventPayload(UUID requestId, UUID customerId, Status status) {
        this.requestId = requestId;
        this.customerId = customerId;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }

    public UUID getRequestId() {
        return requestId;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public Status getStatus() {
        return status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String getKey() {
        return requestId != null ? requestId.toString() : "unknown";
    }
}
