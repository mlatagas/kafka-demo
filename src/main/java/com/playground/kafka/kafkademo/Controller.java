package com.playground.kafka.kafkademo;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/kafka")
@RestController
@Slf4j
public class Controller {
    final KafkaTemplate<String, Object> kafkaTemplate;

    public Controller(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping
    public ResponseEntity<String> sentMessage(@RequestBody Account account) {
        this.kafkaTemplate.send("transaction-1", new Account(account.getHolder(), account.getFunds()));
        return ResponseEntity.ok("done");

    }

    @KafkaListener(topics = "transaction-1")
    public void listener(@Payload Account account, ConsumerRecord<String, Account> cr) {
        log.info("Topic [transaction-1] Received message from {} with {} PLN", account.getHolder(), account.getFunds());
        log.info(cr.toString());
    }
}
