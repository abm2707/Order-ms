package com.akhil.orders.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.akhil.common.events.OrderCreatedEvent;
import org.akhil.common.events.PaymentRequestedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class PaymentEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(PaymentEventPublisher.class);
    private static final String PAYMENT_REQUESTED_TOPIC = "payment.requested";
    private final KafkaTemplate<String, Object> kafkaTemplate;
    ObjectMapper objectMapper = new ObjectMapper();

    public PaymentEventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishPaymentRequest(PaymentRequestedEvent event) {
        //log.info("Producing OrderCreatedEvent: {}", objectMapper.writeValueAsString(event));
        kafkaTemplate.send(
                PAYMENT_REQUESTED_TOPIC,
                event.getOrderId().toString(),
                event
        );
    }
}
