package com.acme.insurance.infrastructure.kafka.producer;

import com.acme.insurance.application.usecase.KafkaMessageKeyAware;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.Mockito.*;

public class KafkaPolicyEventPublisherTest {

    private KafkaTemplate<String, KafkaMessageKeyAware> kafkaTemplate;
    private KafkaPolicyEventPublisher<KafkaMessageKeyAware> publisher;

    @BeforeEach
    void setup() {
        kafkaTemplate = mock(KafkaTemplate.class);
        publisher = new KafkaPolicyEventPublisher<>(kafkaTemplate);
    }

    @Test
    void shouldPublishMessageToKafkaWithCorrectKeyAndTopic() {
        String topic = "policy-events";
        KafkaMessageKeyAware event = mock(KafkaMessageKeyAware.class);
        when(event.getKey()).thenReturn("policy-key-123");

        publisher.publish(topic, event);

        verify(kafkaTemplate).send(topic, "policy-key-123", event);
    }
}
