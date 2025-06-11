package com.acme.insurance.application.service;

import com.acme.insurance.application.port.out.EventPublisher;
import com.acme.insurance.application.usecase.PolicyEventPayload;
import com.acme.insurance.domain.model.PolicyRequest;
import com.acme.insurance.domain.model.Status;
import org.springframework.stereotype.Component;

@Component
public class PolicyRequestEventPublisher {

    private final EventPublisher<PolicyEventPayload> eventPublisher;

    public PolicyRequestEventPublisher(EventPublisher<PolicyEventPayload> eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void publishByStatus(PolicyRequest request) {
        Status status = request.getStatus();
        String topic = "policy-request." + status.name().toLowerCase();
        eventPublisher.publish(topic, toPayload(request));
    }

    public void publishStatusChanged(PolicyRequest request) {
        String topicSuffix = request.getStatus().name().toLowerCase();
        String topic = "policy-request." + topicSuffix;
        eventPublisher.publish(topic, toPayload(request));
    }

    private PolicyEventPayload toPayload(PolicyRequest request) {
        return new PolicyEventPayload(
                request.getId(),
                request.getCustomerId(),
                request.getStatus()
        );
    }
}
