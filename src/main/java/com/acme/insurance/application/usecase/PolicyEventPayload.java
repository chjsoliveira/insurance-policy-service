package com.acme.insurance.application.usecase;
import com.acme.insurance.domain.model.Status;

import java.time.LocalDateTime;
import java.util.UUID;

public class PolicyEventPayload {
    private UUID id;
    private UUID customerId;
    private Status status;
    private LocalDateTime timestamp;

    public PolicyEventPayload(UUID id, UUID customerId, Status status) {
        this.id = id;
        this.customerId = customerId;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }

    // Getters
    public UUID getId() {
        return id;
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
}
