package com.acme.insurance.infrastructure.kafka.config;

import com.acme.insurance.application.usecase.PolicyEventPayload;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Bean
    public ProducerFactory<String, PolicyEventPayload> policyEventProducerFactory() {
        return new DefaultKafkaProducerFactory<>(
            Map.of(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:29092",
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class
            )
        );
    }

    @Bean
    public KafkaTemplate<String, PolicyEventPayload> policyEventKafkaTemplate(
        ProducerFactory<String, PolicyEventPayload> factory) {
        return new KafkaTemplate<>(factory);
    }
	
    @Bean
    public ProducerFactory<String, String> stringProducerFactory() {
        return new DefaultKafkaProducerFactory<>(
            Map.of(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:29092",
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class
            )
        );
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(stringProducerFactory());
    }	
}
