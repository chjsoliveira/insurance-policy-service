package com.acme.insurance.application.port.out;

public interface EventPublisher<T> {
    void publish(String topic, T event);
}