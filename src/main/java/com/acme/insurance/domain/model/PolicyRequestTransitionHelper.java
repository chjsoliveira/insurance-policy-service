package com.acme.insurance.domain.model;

import java.time.LocalDateTime;

public class PolicyRequestTransitionHelper {

    public static void markAsReceived(PolicyRequest request) {
        if (request.getStatus() != null ) {
            throw new IllegalStateException("Somente solicitações sem status (nulo) podem avançar para o status RECEBIDO.");
        }
        request.updateStatus(Status.RECEIVED);
    }
    public static void markAsValidated(PolicyRequest request) {
        if (request.getStatus() != Status.RECEIVED ) {
            throw new IllegalStateException("Somente solicitações com status RECEBIDO podem avançar para o status VALIDADO.");
        }
        request.updateStatus(Status.VALIDATED);
    }

    public static void markAsPending(PolicyRequest request) {
        if (request.getStatus() != Status.VALIDATED) {
            throw new IllegalStateException("Somente solicitações com status VALIDADO podem avançar para o status PENDENTE.");
        }
        request.updateStatus(Status.PENDING);
    }

    public static void markAsApproved(PolicyRequest request) {
        if (request.getStatus() != Status.PENDING) {
            throw new IllegalStateException("Somente solicitações com status PENDENTE podem ser APROVADAS.");
        }
        request.setFinishedAt(LocalDateTime.now());
        request.updateStatus(Status.APPROVED);
    }

    public static void markAsRejected(PolicyRequest request) {
        if (request.getStatus() != Status.RECEIVED && request.getStatus() != Status.PENDING) {
            throw new IllegalStateException("Somente solicitações com status RECEBIDO ou PENDENTE podem ser REJEITADAS.");
        }
        request.setFinishedAt(LocalDateTime.now());
        request.updateStatus(Status.REJECTED);
    }

    public static void rejectFromValidation(PolicyRequest request) {
        if (request.getStatus() != Status.RECEIVED) {
            throw new IllegalStateException("Somente solicitações com status RECEBIDO podem ser rejeitadas durante a validação.");
        }
        request.setFinishedAt(LocalDateTime.now());
        request.updateStatus(Status.REJECTED);
    }

    public static void markAsCancelled(PolicyRequest request) {
        if (request.getStatus() == Status.APPROVED || request.getStatus() == Status.REJECTED || request.getStatus() == Status.CANCELLED) {
            throw new IllegalStateException("Não é possível cancelar uma solicitação que já foi APROVADA, REJEITADA ou CANCELADA.");
        }
        request.setFinishedAt(LocalDateTime.now());
        request.updateStatus(Status.CANCELLED);
    }
}