package com.acme.insurance.infrastructure.kafka.producer;

import com.acme.insurance.application.port.out.EventPublisher;
import com.acme.insurance.application.usecase.KafkaMessageKeyAware;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaPolicyEventPublisher<T extends KafkaMessageKeyAware> implements EventPublisher<T> {

    private final KafkaTemplate<String, T> kafkaTemplate;
    private static final Logger logger = LoggerFactory.getLogger(KafkaPolicyEventPublisher.class);

    public KafkaPolicyEventPublisher(KafkaTemplate<String, T> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publish(String topic, T event) {
        String key = event.getKey();
        logger.info("Kafka - publicando evento no tópico [{}] com chave [{}]: {}", topic, key, event);
        kafkaTemplate.send(topic, key, event);
    }
}
