package com.acme.insurance.api;

import com.acme.insurance.messaging.producer.KafkaProducerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/simulacoes")
public class SimulationController {
    private final KafkaProducerService producer;

    public SimulationController(KafkaProducerService producer) {
        this.producer = producer;
    }

    @PostMapping("/pagamento")
    public ResponseEntity<Void> simulatePayment(@RequestParam UUID requestId) {
        String json = "{" + "\"requestId\":\"" + requestId + "\",\"amount\":100.0}";
        producer.send("payment.received", json);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/subscricao")
    public ResponseEntity<Void> simulateSubscription(@RequestParam UUID requestId) {
        String json = "{" + "\"requestId\":\"" + requestId + "\",\"authorized\":true}";
        producer.send("subscription.created", json);
        return ResponseEntity.ok().build();
    }
}