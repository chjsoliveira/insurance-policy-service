package com.acme.insurance.domain.model;

import java.time.LocalDateTime;

public class PolicyRequestTransitionHelper {

    public static void markAsReceived(PolicyRequest request) {
        if (request.getStatus() != null ) {
            throw new IllegalStateException("Only null requests can move to RECEIVED.");
        }
        request.updateStatus(Status.RECEIVED);
    }
    public static void markAsValidated(PolicyRequest request) {
        if (request.getStatus() != Status.RECEIVED ) {
            throw new IllegalStateException("Only RECEIVED  requests can move to VALIDATED.");
        }
        request.updateStatus(Status.VALIDATED);
    }

    public static void markAsPending(PolicyRequest request) {
        if (request.getStatus() != Status.VALIDATED) {
            throw new IllegalStateException("Only VALIDATED requests can move to PENDING.");
        }
        request.updateStatus(Status.PENDING);
    }

    public static void markAsApproved(PolicyRequest request) {
        if (request.getStatus() != Status.PENDING) {
            throw new IllegalStateException("Only PENDING requests can be APPROVED.");
        }
        request.setFinishedAt(LocalDateTime.now());
        request.updateStatus(Status.APPROVED);
    }

    public static void markAsRejected(PolicyRequest request) {
        if (request.getStatus() != Status.RECEIVED && request.getStatus() != Status.PENDING) {
            throw new IllegalStateException("Only RECEIVED or PENDING requests can be REJECTED.");
        }
        request.setFinishedAt(LocalDateTime.now());
        request.updateStatus(Status.REJECTED);
    }

    public static void rejectFromValidation(PolicyRequest request) {
        if (request.getStatus() != Status.RECEIVED) {
            throw new IllegalStateException("Only RECEIVED requests can be rejected from validation.");
        }
        request.setFinishedAt(LocalDateTime.now());
        request.updateStatus(Status.REJECTED);
    }

    public static void markAsCancelled(PolicyRequest request) {
        if (request.getStatus() == Status.APPROVED || request.getStatus() == Status.REJECTED || request.getStatus() == Status.CANCELLED) {
            throw new IllegalStateException("Cannot cancel an already APPROVED or REJECTED or CANCELLED request.");
        }
        request.setFinishedAt(LocalDateTime.now());
        request.updateStatus(Status.CANCELLED);
    }
}