package com.acme.insurance.application.usecase;

public interface KafkaMessageKeyAware {
    String getKey();
}
