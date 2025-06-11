package com.acme.insurance.infrastructure.config;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class ObservabilityConfig {

    private final MeterRegistry meterRegistry;

    public ObservabilityConfig(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @PostConstruct
    public void setup() {
        meterRegistry.config().commonTags("application", "insurance-policy-service");
    }
}
