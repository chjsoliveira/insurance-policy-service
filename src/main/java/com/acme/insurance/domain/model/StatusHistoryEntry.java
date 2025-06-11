package com.acme.insurance.domain.model;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

@Embeddable
public class StatusHistoryEntry {

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime timestamp;

    public StatusHistoryEntry() {
    }

    public StatusHistoryEntry(Status status, LocalDateTime timestamp) {
        this.status = status;
        this.timestamp = timestamp;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
